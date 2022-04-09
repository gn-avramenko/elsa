/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.server.core.storage.jdbc.DataSourceProvider;
import com.gridnine.elsa.server.core.storage.jdbc.JdbcDialect;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.stereotype.Component;

@Component
public class HsqldbDataSourceProvider implements DataSourceProvider {

    private ComboPooledDataSource ds;

    @Override
    public ComboPooledDataSource createDataSource() throws Exception {
        ds = new ComboPooledDataSource();
        ds.setDriverClass(JDBCDriver.class.getName());
        ds.setJdbcUrl("jdbc:hsqldb:mem:elsa;shutdown=true");
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
    public JdbcDialect createDialect() {
        return new HsqldbDialect();
    }

    @Override
    public String getId() {
        return "hsqldb";
    }

}
