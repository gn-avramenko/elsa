/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexDescription;

import java.sql.ResultSet;
import java.util.Map;

public interface JdbcFieldHandler {
    Map<String, String> getColumns();
    Map<String, JdbcIndexDescription> getIndexes(String tableName);
    Object getModelValue(ResultSet rs) throws Exception;
    Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception;
    Pair<Object, String> getSqlQueryValue(Object value) throws Exception;

    default void setValue(BaseIntrospectableObject obj, String propertyName, Object value){
        obj.setValue(propertyName, value);
    }

}
