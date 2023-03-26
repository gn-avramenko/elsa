/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;

import java.util.Collections;
import java.util.Map;

abstract class BaseJdbcSingleFieldHandler implements JdbcFieldHandler {
    protected final String fieldName;
    private final boolean indexed;
    protected final String type;
    public BaseJdbcSingleFieldHandler(String fieldName, boolean indexed, String type){
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.type = type;
    }

    @Override
    public Map<String, String> getColumns() {
        return Collections.singletonMap(fieldName, type) ;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new JdbcIndexDescription(fieldName, getIndexType())) :
                Collections.emptyMap();
    }

    protected JdbcIndexType getIndexType() {
        return JdbcIndexType.BTREE;
    }


}
