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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;

public class LocalDateDatabaseFieldHandler extends BaseDatabaseSingleFieldHandler{

    public LocalDateDatabaseFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlType.DATE);
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : LocalDate.ofInstant(Instant.ofEpochMilli((rs.getDate(fieldName)).getTime()), ZoneId.systemDefault());
    }

    @Override
    public Map<String, Pair<Object,SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Collections.singletonMap(fieldName, Pair.of(value == null? null : new Date(((LocalDate) value)
                .atTime(0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlType.DATE));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Pair.of(value == null? null : new Date(((LocalDate) value)
                .atTime(0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlType.DATE);
    }
}
