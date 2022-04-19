/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDataSourceProvider;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hsqldb.jdbc.JDBCDriver;

import javax.sql.DataSource;

public class HsqldbDataSourceProvider implements JdbcDataSourceProvider {

    @Override
    public ComboPooledDataSource createDataSource() throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(JDBCDriver.class.getName());
        ds.setJdbcUrl("jdbc:hsqldb:mem:elsa;shutdown=true");
//        ds.setJdbcUrl("jdbc:hsqldb:file:/home/avramenko/IdeaProjects/own/elsa/temp/db/elsa;shutdown=true");
        ds.setInitialPoolSize(1);
        ds.setAcquireIncrement(5);
        ds.setMinPoolSize(1);
        ds.setMaxPoolSize(5);
        ds.setUser("SA");
        ds.setPassword("");
        ds.setAutoCommitOnClose(false);
        return ds;
    }

    @Override
    public JdbcDialect createDialect(DataSource ds) {
        return new HsqldbDialect(ds);
    }

    @Override
    public String getId() {
        return "hsqldb";
    }

}
