/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

public class JdbcLongFieldHandler extends BaseJdbcSingleFieldHandler {

    public JdbcLongFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlTypeLongHandler.type);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : rs.getLong(fieldName);
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(value, SqlTypeLongHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value, SqlTypeLongHandler.type);
    }
}
