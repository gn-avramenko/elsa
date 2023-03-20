/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeBigDecimalHandler implements SqlTypeHandler<BigDecimal> {

    public final static String type = "BIGDECIMAL";
    @Override
    public void setValue(PreparedStatement ps, int idx, BigDecimal value) throws Exception {
        ps.setBigDecimal(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.NUMERIC;
    }
}
