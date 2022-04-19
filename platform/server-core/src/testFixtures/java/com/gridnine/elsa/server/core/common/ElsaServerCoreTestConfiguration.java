/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.server.core.storage.database.jdbc.adapter.JdbcDataSourceProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ElsaServerCoreTestConfiguration {

    @Bean
    public JdbcDataSourceProvider hsqldbProvider() {
        return new HsqldbDataSourceProvider();
    }

    @Bean
    public TestDomainDocumentProjectionHandler testDomainDocumentProjectionHandler(){
        return new TestDomainDocumentProjectionHandler();
    }
}
