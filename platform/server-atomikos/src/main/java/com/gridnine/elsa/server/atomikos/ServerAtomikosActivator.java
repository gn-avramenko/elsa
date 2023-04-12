/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcDialect;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;

import javax.sql.DataSource;

public class ServerAtomikosActivator implements Activator {
    @Override
    public double getOrder() {
        return 4;
    }

    @Override
    public void configure() throws Exception {
        Environment.unpublish(TransactionManager.class);
        Environment.publish(TransactionManager.class, new AtomikosTransactionManager());
        var ds = new AtomikosDataSourceBean();
        ds.setPoolSize(Integer.parseInt(Configuration.get().getValue("repository.atomikos.poolSize", "10")));
        ds.setXaDataSource(JdbcDialect.get().createXADataSource());
        ds.setUniqueResourceName("main");
        ds.setLocalTransactionMode(true);
        ds.init();
        Environment.unpublish(DataSource.class);
        Environment.publish(DataSource.class, ds);
        Environment.publish((Disposable) ds::close);

    }

    @Override
    public void activate() throws Exception {

    }
}
