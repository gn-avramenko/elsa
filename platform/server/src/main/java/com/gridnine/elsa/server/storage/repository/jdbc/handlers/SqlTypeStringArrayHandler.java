/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collection;

public class SqlTypeStringArrayHandler implements SqlTypeHandler<Collection<Object>> {

    public final static String type = "STRING_ARRAY";
    @Override
    public void setValue(PreparedStatement ps, int idx, Collection<Object> value) throws Exception {
        var array = ps.getConnection().createArrayOf("varchar", value.toArray());
        ps.setArray(idx, array);
    }

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }
}
