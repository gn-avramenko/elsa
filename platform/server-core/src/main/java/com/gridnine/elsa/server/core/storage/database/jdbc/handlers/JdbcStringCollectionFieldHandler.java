/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.handlers;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexDescription;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexType;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class JdbcStringCollectionFieldHandler implements JdbcFieldHandler {

    private final String fieldName;
    private final boolean indexed;

    public JdbcStringCollectionFieldHandler(String fieldName, boolean indexed){
        this.fieldName = fieldName;
        this.indexed = indexed;
    }

    @Override
    public Map<String, JdbcFieldType> getColumns() {
        return Collections.singletonMap(fieldName, JdbcFieldType.STRING_ARRAY) ;
    }

    @Override
    public Map<String, JdbcIndexDescription> getIndexes(String tableName) {
        return indexed? Collections.singletonMap("%s_%s".formatted(tableName, fieldName), new JdbcIndexDescription(fieldName, JdbcIndexType.BTREE)) :
                Collections.emptyMap();
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialiect) throws Exception {
        var jdbcValue = rs.getArray(fieldName);
        if(jdbcValue == null){
            return Collections.emptyList();
        }
        return Arrays.stream((Object[]) jdbcValue.getArray()).map(it -> (String) it).toList();
    }

    @Override
    public Map<String, Pair<Object, JdbcFieldType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Collections.singletonMap(fieldName, Pair.of(value, JdbcFieldType.STRING_ARRAY));
    }

    @Override
    public Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value, JdbcFieldType.STRING);
    }

}
