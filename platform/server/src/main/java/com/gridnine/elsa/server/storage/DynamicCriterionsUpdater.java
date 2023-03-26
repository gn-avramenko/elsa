/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.common.search.BaseQuery;
import com.gridnine.elsa.common.search.DynamicCriterion;
import com.gridnine.elsa.common.search.DynamicCriterionHandler;
import com.gridnine.elsa.common.search.JunctionCriterion;
import com.gridnine.elsa.common.search.NotCriterion;
import com.gridnine.elsa.common.search.SearchCriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicCriterionsUpdater {
    private final ConcurrentHashMap<String, DynamicCriterionHandler<Object>> criterionHandlersCache = new ConcurrentHashMap<>();


    <T extends BaseQuery> T updateQuery(String listId, T query){
        updateQueryInternal(listId, query.getCriterions());
        return query;
    }

    private SearchCriterion getCriterionInternal(String listId, SearchCriterion crit) {
        if(crit instanceof DynamicCriterion dc){
            var key = "%s||%s||%s".formatted(listId,dc.propertyId, dc.conditionId);
            DynamicCriterionHandler<Object> handler = criterionHandlersCache.get(key);
            if(handler == null){
                handler = StorageRegistry.get().getDynamicCriterionHandlers().stream().filter(dch -> dch.isApplicable(listId, dc.propertyId)).findFirst().get();
                criterionHandlersCache.put(key, handler);
            }
            return handler.getCriterion(listId, dc.propertyId, dc.conditionId, dc.value);
        }
        if(crit instanceof JunctionCriterion jc){
            var subcrits = new ArrayList<>(jc.criterions);
            if(updateQueryInternal(listId, subcrits)){
                return new JunctionCriterion(jc.disjunction, subcrits);
            }
        }
        if(crit instanceof NotCriterion nc){
            var modifiedCrit = getCriterionInternal(listId, nc.criterion);
            if(modifiedCrit != null){
                return new NotCriterion(modifiedCrit);
            }
        }
        return null;
    }
    private boolean updateQueryInternal(String listId, List<SearchCriterion> criterions) {
        var modified = false;
        var updatedCriterions = new HashMap<SearchCriterion, SearchCriterion>();
        for(SearchCriterion crit : criterions){
            var modifiedCrit = getCriterionInternal(listId, crit);
            if(modifiedCrit != null){
                updatedCriterions.put(crit, modifiedCrit);
                modified = true;
            }
        }
        updatedCriterions.forEach((sourceCrit, destCrit) ->{
            var idx = criterions.indexOf(sourceCrit);
            criterions.set(idx, destCrit);
        });
        return modified;
    }
}
