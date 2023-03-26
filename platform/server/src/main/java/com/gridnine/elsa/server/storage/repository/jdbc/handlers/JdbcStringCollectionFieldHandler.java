/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcFieldHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class JdbcStringCollectionFieldHandler implements JdbcFieldHandler {

    private final String fieldName;
    private final boolean indexed;

    public JdbcStringCollectionFieldHandler(String fieldName, boolean indexed){
        this.fieldName = fieldName;
        this.indexed = indexed;
    }

    @Override
    public Map<String, String> getColumns() {
        return Collections.singletonMap(fieldName, SqlTypeStringArrayHandler.type) ;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new JdbcIndexDescription(fieldName, JdbcIndexType.BTREE)) :
                Collections.emptyMap();
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        var jdbcValue = rs.getArray(fieldName);
        if(jdbcValue == null){
            return Collections.emptyList();
        }
        return Arrays.stream((Object[]) jdbcValue.getArray()).toList();
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(value, SqlTypeStringArrayHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value, SqlTypeStringHandler.type);
    }

    @Override
    public void setValue(BaseIntrospectableObject obj, String propertyName, Object value) {
        Collection<String> coll = (Collection<String>) obj.getValue(propertyName);
        coll.clear();
        if(value != null) {
            coll.addAll((Collection<? extends String>) value);
        }
    }
}
