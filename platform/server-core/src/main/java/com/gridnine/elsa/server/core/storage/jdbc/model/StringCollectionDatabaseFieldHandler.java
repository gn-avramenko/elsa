/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.server.core.storage.jdbc.JdbcDialect;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class StringCollectionDatabaseFieldHandler implements DatabaseFieldHandler{

    private final String fieldName;
    private final boolean indexed;

    public StringCollectionDatabaseFieldHandler(String fieldName, boolean indexed){
        this.fieldName = fieldName;
        this.indexed = indexed;
    }

    @Override
    public Map<String, SqlType> getColumns() {
        return Collections.singletonMap(fieldName, SqlType.STRING_ARRAY) ;
    }

    @Override
    public Map<String, IndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new IndexDescription(fieldName, IndexType.BTREE)) :
                Collections.emptyMap();
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialiect) throws Exception {
        var jdbcValue = rs.getArray(fieldName);
        if(jdbcValue == null){
            return Collections.emptyList();
        }
        return Arrays.asList((String[]) jdbcValue.getArray());
    }

    @Override
    public Map<String, Pair<Object, SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Collections.singletonMap(fieldName, Pair.of(value, SqlType.STRING_ARRAY));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value, SqlType.STRING);
    }

}
