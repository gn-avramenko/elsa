/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseTableDescription {
    private String name;
    private final Map<String, DatabaseFieldHandler> fields = new LinkedHashMap<>();

    public DatabaseTableDescription(String id) {
        this.name = id;
    }

    public String getName() {
        return name;
    }

    public Map<String, DatabaseFieldHandler> getFields() {
        return fields;
    }
}
