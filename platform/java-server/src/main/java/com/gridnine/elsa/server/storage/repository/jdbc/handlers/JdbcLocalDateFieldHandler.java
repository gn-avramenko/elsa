/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.core.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;

public class JdbcLocalDateFieldHandler extends BaseJdbcSingleFieldHandler {


    public JdbcLocalDateFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlTypeDateHandler.type);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : LocalDate.ofInstant(Instant.ofEpochMilli((rs.getTimestamp(fieldName)).getTime()), ZoneId.systemDefault());
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) {
        return Collections.singletonMap(fieldName, new Pair<>(value == null? null : new Date(((LocalDate) value).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlTypeDateHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) {
        return new Pair(value == null? null : new Date(((LocalDate) value).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), SqlTypeDateHandler.type);
    }
}
