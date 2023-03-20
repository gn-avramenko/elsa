/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

public class SqlTypeTimestampHandler implements SqlTypeHandler<Timestamp> {

    public final static String type = "TIMESTAMP";
    @Override
    public void setValue(PreparedStatement ps, int idx, Timestamp value) throws Exception {
        ps.setTimestamp(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.TIMESTAMP;
    }
}
