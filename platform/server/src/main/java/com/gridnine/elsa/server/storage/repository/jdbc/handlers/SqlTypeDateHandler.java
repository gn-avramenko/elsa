/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;

public class SqlTypeDateHandler implements SqlTypeHandler<Date> {

    public final static String type = "DATE";
    @Override
    public void setValue(PreparedStatement ps, int idx, Date value) throws Exception {
        ps.setDate(idx, new java.sql.Date(value.getTime()));
    }

    @Override
    public int getSqlType() {
        return Types.DATE;
    }
}
