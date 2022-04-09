/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import java.util.Collections;
import java.util.Map;

abstract class BaseDatabaseSingleFieldHandler implements DatabaseFieldHandler{
    protected final String fieldName;
    private final boolean indexed;
    protected final SqlType type;
    public BaseDatabaseSingleFieldHandler(String fieldName, boolean indexed, SqlType type){
        this.fieldName = fieldName;
        this.indexed = indexed;
        this.type = type;
    }

    @Override
    public Map<String, SqlType> getColumns() {
        return Collections.singletonMap(fieldName, type) ;
    }

    @Override
    public Map<String, IndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new IndexDescription(fieldName, getIndexType())) :
                Collections.emptyMap();
    }

    protected IndexType getIndexType() {
        return IndexType.BTREE;
    }


}
