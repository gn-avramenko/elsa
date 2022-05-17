/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.adapter;


import javax.sql.DataSource;

import java.util.Map;

public interface JdbcDataSourceProvider {
    DataSource createDataSource(Map<String,Object> properties) throws Exception;
    JdbcDialect createDialect(DataSource ds);
    String getId();
}