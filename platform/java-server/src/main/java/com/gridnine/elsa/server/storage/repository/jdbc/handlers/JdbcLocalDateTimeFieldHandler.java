/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.core.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;

public class JdbcLocalDateTimeFieldHandler extends BaseJdbcSingleFieldHandler {

    public final static String FIELD_TYPE = "LOCAL_DATE_TIME";

    public JdbcLocalDateTimeFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, FIELD_TYPE);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName)? null : LocalDateTime.ofInstant(Instant.ofEpochMilli((rs.getDate(fieldName)).getTime()), ZoneId.systemDefault());
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) {
        return Collections.singletonMap(fieldName, new Pair<>(value == null? null : new Timestamp(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), FIELD_TYPE));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) {
        return new Pair(value == null? null : new Timestamp(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), FIELD_TYPE);
    }
}
