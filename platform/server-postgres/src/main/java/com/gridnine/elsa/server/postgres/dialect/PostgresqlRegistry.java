/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres.dialect;

import com.gridnine.elsa.meta.config.Environment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostgresqlRegistry {
    private final Map<String, PostgresqlTypesHandler> handlers = new ConcurrentHashMap<>();

    public void register(String id, PostgresqlTypesHandler handler){
        handlers.put(id, handler);
    }
    public Collection<PostgresqlTypesHandler> getHandlers() {
        return handlers.values();
    }

    public static PostgresqlRegistry get(){
        return Environment.getPublished(PostgresqlRegistry.class);
    }
}
