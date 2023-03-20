/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeBlobHandler implements SqlTypeHandler<Blob> {

    public final static String type = "BLOB";
    @Override
    public void setValue(PreparedStatement ps, int idx, Blob value) throws Exception {
        ps.setBlob(idx, value);
    }

    @Override
    public int getSqlType() {
        return Types.LONGVARBINARY;
    }
}
