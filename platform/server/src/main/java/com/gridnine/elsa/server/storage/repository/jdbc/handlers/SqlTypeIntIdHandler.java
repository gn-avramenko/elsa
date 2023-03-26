/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeIntIdHandler implements SqlTypeHandler<Integer> {

    public final static String type = "INT_ID";
    @Override
    public void setValue(PreparedStatement ps, int idx, Integer value) throws Exception {
        ps.setInt(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.INTEGER;
    }
}
