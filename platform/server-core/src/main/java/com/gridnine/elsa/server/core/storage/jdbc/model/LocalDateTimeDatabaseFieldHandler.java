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

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;

public class LocalDateTimeDatabaseFieldHandler extends BaseDatabaseSingleFieldHandler{

    public LocalDateTimeDatabaseFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlType.DATE_TIME);
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : LocalDateTime.ofInstant(Instant.ofEpochMilli((rs.getDate(fieldName)).getTime()), ZoneId.systemDefault());
    }

    @Override
    public Map<String, Pair<Object,SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Collections.singletonMap(fieldName, Pair.of(value == null? null : new Timestamp(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlType.DATE_TIME));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Pair.of(value == null? null : new Timestamp(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlType.DATE_TIME);
    }
}
