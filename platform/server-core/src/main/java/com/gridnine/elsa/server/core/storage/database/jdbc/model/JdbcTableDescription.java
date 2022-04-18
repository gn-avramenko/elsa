/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.model;

import com.gridnine.elsa.server.core.storage.database.jdbc.handlers.JdbcFieldHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class JdbcTableDescription {
    private final String name;
    private final Map<String, JdbcFieldHandler> fields = new LinkedHashMap<>();

    public JdbcTableDescription(String id) {
        this.name = id;
    }

    public String getName() {
        return name;
    }

    public Map<String, JdbcFieldHandler> getFields() {
        return fields;
    }
}
