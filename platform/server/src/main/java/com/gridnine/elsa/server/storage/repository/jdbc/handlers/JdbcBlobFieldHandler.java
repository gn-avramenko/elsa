/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;

public class JdbcBlobFieldHandler extends BaseJdbcSingleFieldHandler {

    public JdbcBlobFieldHandler(String fieldName) {
        super(fieldName, false, SqlTypeBlobHandler.type);
    }

    @Override
    public Object getModelValue(ResultSet rs) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : JdbcDialect.get().readBlob(rs, fieldName);
    }

    @Override
    public Map<String, Pair<Object, String>> getSqlValues(Object value) throws Exception {
        return Collections.singletonMap(fieldName, new Pair<>(value, SqlTypeBlobHandler.type));
    }

    @Override
    public Pair<Object, String> getSqlQueryValue(Object value) throws Exception {
        return new Pair<>(value, SqlTypeBlobHandler.type);
    }
}
