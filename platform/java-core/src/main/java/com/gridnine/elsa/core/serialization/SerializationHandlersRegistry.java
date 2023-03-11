/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.serialization;

import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerializationHandlersRegistry {

    private final Map<String,SerializationHandler<?>> handlers = new LinkedHashMap<>();

    public<T> void register(SerializationHandler<T> handler){
        handlers.put(handler.getType(), handler);
    }

    public SerializationHandler<Object> getHandler(String type){
        return (SerializationHandler<Object>) handlers.get(type);
    }

    public static SerializationHandlersRegistry get(){
        return Environment.getPublished(SerializationHandlersRegistry.class);
    }
}
