/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.adapter;


import javax.sql.DataSource;

public interface JdbcDataSourceProvider {
    DataSource createDataSource() throws Exception;
    JdbcDialect createDialect();
    String getId();
}
