/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.postgres.activator;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.postgres.dialect.PostgresDialect;
import com.gridnine.elsa.server.postgres.dialect.PostgresqlRegistry;
import com.gridnine.elsa.server.postgres.dialect.StandardPostgresqlTypesHandler;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.postgresql.Driver;

import javax.sql.DataSource;

public class PostgresqlActivator implements Activator {
    @Override
    public double getOrder() {
        return 4;
    }

    @Override
    public void configure() throws Exception {
        if(!"POSTGRESQL".equals(Configuration.get().getValue("repository.type", "POSTGRESQL"))){
            return;
        }
        Configuration config = Configuration.get().getSubConfiguration("repository.postgresql");
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(Driver.class.getName());
        ds.setJdbcUrl(config.getValue("url"));
        ds.setInitialPoolSize(1);
        ds.setAcquireIncrement(5);
        ds.setMinPoolSize(1);
        ds.setMaxPoolSize(Integer.parseInt(config.getValue("poolSize", "50")));
        ds.setUser(config.getValue("login"));
        ds.setPassword(config.getValue("password"));
        ds.setAutoCommitOnClose(false);
        Environment.publish(DataSource.class, ds);
        Environment.publish((Disposable) ds::close);
        Environment.publish(JdbcDialect.class, new PostgresDialect());
        Environment.publish(new PostgresqlRegistry());
        PostgresqlRegistry.get().register("standard", new StandardPostgresqlTypesHandler());
    }

    @Override
    public void activate() throws Exception {

    }
}
