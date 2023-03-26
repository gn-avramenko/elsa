/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import java.sql.PreparedStatement;

public interface SqlTypeHandler<T> {

    void setValue(PreparedStatement ps, int idx, T value) throws Exception;

    int getSqlType();
}
