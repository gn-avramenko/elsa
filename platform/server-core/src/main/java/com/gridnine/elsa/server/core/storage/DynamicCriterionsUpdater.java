/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.search.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicCriterionsUpdater {
    private final ConcurrentHashMap<String,DynamicCriterionHandler<Object>> criterionHandlersCache = new ConcurrentHashMap<>();

    private final List<DynamicCriterionHandler<Object>> handlers;
    public  DynamicCriterionsUpdater(List<DynamicCriterionHandler<Object>> handlers){
        this.handlers = handlers;
    }

    <T extends BaseQuery> T updateQuery(String listId, T query){
        updateQueryInternal(listId, query.getCriterions());
        return query;
    }

    private SearchCriterion getCriterionInternal(String listId, SearchCriterion crit) {
        if(crit instanceof DynamicCriterion dc){
            var key = "%s||%s||%s".formatted(listId,dc.propertyId, dc.conditionId);
            var handler = handlers.stream().filter(dch -> dch.isApplicable(listId, dc.propertyId)).findFirst().get();
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
