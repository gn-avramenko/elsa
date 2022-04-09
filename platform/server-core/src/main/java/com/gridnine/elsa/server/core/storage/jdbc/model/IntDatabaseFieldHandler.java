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
import java.util.Collections;
import java.util.Map;

public class IntDatabaseFieldHandler extends BaseDatabaseSingleFieldHandler{

    public IntDatabaseFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlType.INT);
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : rs.getInt(fieldName);
    }

    @Override
    public Map<String, Pair<Object, SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Collections.singletonMap(fieldName, Pair.of(value, SqlType.INT));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value, SqlType.INT);
    }
}
