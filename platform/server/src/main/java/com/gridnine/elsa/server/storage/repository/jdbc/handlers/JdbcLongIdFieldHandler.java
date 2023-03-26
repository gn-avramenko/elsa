/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.BaseIdentity;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

public class JdbcLongIdFieldHandler extends BaseJdbcSingleFieldHandler {

    public JdbcLongIdFieldHandler() {
        super(BaseIdentity.Fields.id, false, SqlTypeLongIdHandler.type);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : rs.getLong(fieldName);
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(value, SqlTypeLongIdHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value, SqlTypeLongHandler.type);
    }
}
