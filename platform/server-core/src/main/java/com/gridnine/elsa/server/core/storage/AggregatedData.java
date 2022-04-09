/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class AggregatedData {

    private Set<String> data = new LinkedHashSet<>();

    private DomainMetaRegistry registry;

    public void aggregate(Object obj){
        if(obj == null){
            return;
        }
        if(obj instanceof Enum<?> en){
            var enumDescr = registry.getEnums().get(obj.getClass().getName());
            if(enumDescr == null){
                data.add(en.name().toLowerCase());
                return;
            }
            var descr = enumDescr.getItems().get(en.name());
            if(descr == null){
                data.add(en.name().toLowerCase());
                return;
            }
            data.addAll(descr.getDisplayNames().values());
            return;
        }
        if(obj instanceof Collection<?> coll){
            for(Object elm: coll){
                aggregate(elm);
            }
            return;
        }
        data.add(obj.toString().toLowerCase());
    }

    public String getAggregatedData(){
        return StringUtils.join(data, " ");
    }

}
