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
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class EnumDatabaseFieldHandler extends BaseDatabaseSingleFieldHandler{

    private final Class<Enum<?>> enumClass;
    public EnumDatabaseFieldHandler(String fieldName, Class<Enum<?>> enumClass, boolean indexed) {
        super(fieldName, indexed, SqlType.INT);
        this.enumClass = enumClass;
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : factory.safeGetEnum(enumClass, enumMapper.getName(rs.getInt(fieldName), enumClass));
    }

    @Override
    public Map<String, Pair<Object,SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Collections.singletonMap(fieldName, Pair.of(value == null? null : enumMapper.getId((Enum<?>) value), SqlType.INT));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Pair.of(value == null? null : enumMapper.getId((Enum<?>) value), SqlType.INT);
    }
}
