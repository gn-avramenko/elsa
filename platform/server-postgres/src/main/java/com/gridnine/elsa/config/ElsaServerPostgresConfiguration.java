/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDataSourceProvider;
import com.gridnine.elsa.server.postgres.PostgresqlDataSourceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerPostgresConfiguration {
    @Bean
    public JdbcDataSourceProvider postgresProvider(){
        return new PostgresqlDataSourceProvider();
    }
}
