/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.server.core.storage.database.DatabaseFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.SimpleJdbcDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerCoreDatabaseFactoryConfiguration {

    @Bean
    public DatabaseFactory databaseFactory(){
        return new SimpleJdbcDatabaseFactory();
    }

}
