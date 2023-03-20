/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeStringHandler implements SqlTypeHandler<String> {

    public final static String type = "STRING";
    @Override
    public void setValue(PreparedStatement ps, int idx, String value) throws Exception {
        ps.setString(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }
}
