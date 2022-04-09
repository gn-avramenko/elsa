/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public interface DataSourceProvider {
    ComboPooledDataSource createDataSource() throws Exception;
    JdbcDialect createDialect();
    String getId();
}
