/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeBooleanHandler implements SqlTypeHandler<Boolean> {

    public final static String type = "BOOLEAN";
    @Override
    public void setValue(PreparedStatement ps, int idx, Boolean value) throws Exception {
        ps.setBoolean(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.BOOLEAN;
    }
}
