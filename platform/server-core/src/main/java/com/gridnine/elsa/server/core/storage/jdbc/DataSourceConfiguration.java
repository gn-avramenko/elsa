/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc;

import com.gridnine.elsa.server.core.storage.jdbc.structureUpdater.JdbcStructureUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DataSourceConfiguration {

    private List<DataSourceProvider> providers;

    private Environment environment;

    @Autowired
    public void setDataSourceProviders(List<DataSourceProvider> providers){
        this.providers = providers;
    }

    @Autowired
    public void setEnvironment(Environment environment){
        this.environment = environment;
    }

    @Bean
    public DataSource createDataSource() throws Exception {
        var providerId = environment.getProperty("jdbc.providerId", "hsqldb");
        var provider = providers.stream().filter(it -> it.getId().equals(providerId)).findFirst().get();
        return provider.createDataSource();
    }

    @Bean
    public JdbcDialect createDialect() throws Exception {
        var providerId = environment.getProperty("jdbc.providerId", "hsqldb");
        var provider = providers.stream().filter(it -> it.getId().equals(providerId)).findFirst().get();
        return provider.createDialect();
    }

    @Bean
    public JdbcStructureUpdater structureUpdater() throws Exception {
       return new JdbcStructureUpdater();
    }


}
