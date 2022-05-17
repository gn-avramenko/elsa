/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres;

import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDataSourceProvider;
import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDialect;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.postgresql.Driver;

import javax.sql.DataSource;
import java.util.Map;

public class PostgresqlDataSourceProvider implements JdbcDataSourceProvider {

    @Override
    public ComboPooledDataSource createDataSource(Map<String,Object> props) throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(Driver.class.getName());
        ds.setJdbcUrl((String) props.get("url"));
        ds.setInitialPoolSize(1);
        ds.setAcquireIncrement(5);
        ds.setMinPoolSize(1);
        ds.setMaxPoolSize((Integer) props.get("poolSize"));
        ds.setUser((String) props.get("login"));
        ds.setPassword((String) props.get("password"));
        ds.setAutoCommitOnClose(false);
        return ds;
    }

    @Override
    public JdbcDialect createDialect(DataSource ds) {
        return new PostgresDialect(ds);
    }

    @Override
    public String getId() {
        return "postgres";
    }

}
