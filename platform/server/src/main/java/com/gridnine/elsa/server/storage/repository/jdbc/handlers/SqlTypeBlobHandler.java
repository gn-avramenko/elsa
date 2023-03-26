/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.handlers;

import com.gridnine.elsa.server.storage.repository.RepositoryBinaryData;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.gridnine.elsa.server.storage.repository.jdbc.SqlTypeHandler;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.Types;

public class SqlTypeBlobHandler implements SqlTypeHandler<RepositoryBinaryData> {

    public final static String type = "BLOB";
    @Override
    public void setValue(PreparedStatement ps, int idx, RepositoryBinaryData value) throws Exception {
       JdbcDialect.get().setBlob(ps, idx, value);;
    }

    @Override
    public int getSqlType() {
        return Types.LONGVARBINARY;
    }
}
