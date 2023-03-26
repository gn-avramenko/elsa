/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.meta.config.Environment;

import java.util.HashMap;
import java.util.Map;

public class JdbcRegistry {

    private final Map<String, JdbcFieldHandlerFactory> jdbcFieldFactoryMap = new HashMap<>();

    private final Map<String, SqlTypeHandler<Object>> sqlTypeHandlersMap = new HashMap<>();

    public void register(String tagName, JdbcFieldHandlerFactory factory){
        jdbcFieldFactoryMap.put(tagName, factory);
    }

    public JdbcFieldHandlerFactory getFieldHandlerFactory(String tagName){
        return jdbcFieldFactoryMap.get(tagName);
    }

    public<T> void register(String type, SqlTypeHandler<T> handler){
        sqlTypeHandlersMap.put(type, (SqlTypeHandler<Object>) handler);
    }

    public SqlTypeHandler<Object> getSqlTypeHandler(String type){
        return sqlTypeHandlersMap.get(type);
    }

    public static JdbcRegistry get(){
        return Environment.getPublished(JdbcRegistry.class);
    }
}


