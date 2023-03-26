/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

import com.gridnine.elsa.meta.config.Environment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HsqldbRegistry {
    private final Map<String, HsqldbTypeHandler> handlers = new ConcurrentHashMap<>();

    public void register(String id, HsqldbTypeHandler handler){
        handlers.put(id, handler);
    }
    public Collection<HsqldbTypeHandler> getHandlers() {
        return handlers.values();
    }

    public static HsqldbRegistry get(){
        return Environment.getPublished(HsqldbRegistry.class);
    }
}
