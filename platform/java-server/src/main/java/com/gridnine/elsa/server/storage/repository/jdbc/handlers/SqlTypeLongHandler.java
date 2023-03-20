/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeLongHandler implements SqlTypeHandler<Long> {

    public final static String type = "LONG";
    @Override
    public void setValue(PreparedStatement ps, int idx, Long value) throws Exception {
        ps.setLong(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.BIGINT;
    }
}
