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

public class BlobDatabaseFieldHandler extends BaseDatabaseSingleFieldHandler{

    public BlobDatabaseFieldHandler(String fieldName) {
        super(fieldName, false, SqlType.BLOB);
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory, JdbcDialect dialect) throws Exception {
        return JdbcUtils.isNull(rs, fieldName)? null : dialect.readBlob(rs, fieldName);
    }

    @Override
    public Map<String, Pair<Object, SqlType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Collections.singletonMap(fieldName, Pair.of(value, SqlType.BLOB));
    }

    @Override
    public Pair<Object, SqlType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) throws Exception {
        return Pair.of(value, SqlType.BLOB);
    }
}
