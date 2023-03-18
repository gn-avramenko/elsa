/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage;

import com.gridnine.elsa.core.utils.TextUtils;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class AggregatedData {

    private final Set<String> data = new LinkedHashSet<>();

    public void aggregate(Object obj){
        if(obj == null){
            return;
        }
        if(obj instanceof Enum<?> en){
            var enumDescr = SerializableMetaRegistry.get().getEnums().get(obj.getClass().getName());
            if(enumDescr == null){
                data.add(en.name().toLowerCase());
                return;
            }
            var descr = enumDescr.getItems().get(en.name());
            if(descr == null){
                data.add(en.name().toLowerCase());
                return;
            }
            data.addAll(descr.getDisplayNames().values().stream().map(String::toLowerCase).toList());
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
        return TextUtils.join(data, " ");
    }

}
