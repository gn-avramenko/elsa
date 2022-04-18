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
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

public class JdbcTextFieldHandler extends BaseJdbcSingleFieldHandler {

    public JdbcTextFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, JdbcFieldType.TEXT);
    }

    @Override
    protected JdbcIndexType getIndexType() {
        return JdbcIndexType.GIN;
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : rs.getString(fieldName);
    }

    @Override
    public Map<String, Pair<Object, JdbcFieldType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Collections.singletonMap(fieldName, Pair.of(value, JdbcFieldType.TEXT));
    }

    @Override
    public Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value, JdbcFieldType.TEXT);
    }
}
