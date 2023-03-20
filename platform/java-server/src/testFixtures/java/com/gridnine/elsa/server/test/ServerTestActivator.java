/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test;

import com.gridnine.elsa.core.config.Activator;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.StorageRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hsqldb.jdbc.JDBCDriver;

import javax.sql.DataSource;

public class ServerTestActivator implements Activator {
    @Override
    public double getOrder() {
        return 3.01;
    }

    @Override
    public void configure() throws Exception {
        StorageRegistry.get().register(new TestDomainDocumentProjectionHandler());
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
        Environment.publish(DataSource.class, ds);
        Environment.publish((Disposable) ds::close);
        Environment.publish(JdbcDialect.class, new HsqldbDialect());
    }

    @Override
    public void activate() throws Exception {
        //noops
    }


}
