/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcIndexType;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

public class JdbcTextFieldHandler extends BaseJdbcSingleFieldHandler {

    public JdbcTextFieldHandler(String fieldName, boolean indexed) {
        super(fieldName, indexed, SqlTypeTextHandler.type);
    }

    @Override
    protected JdbcIndexType getIndexType() {
        return JdbcIndexType.GIN;
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : rs.getString(fieldName);
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(value, SqlTypeTextHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value, SqlTypeTextHandler.type);
    }
}
