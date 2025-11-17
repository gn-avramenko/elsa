/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo.test;

import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.database.jdbc.JdbcDatabaseCustomizer;
import com.gridnine.platform.elsa.server.mongo.StandardMongoJdbcCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerMongoTestConfiguration {
    private final DomainMetaRegistry dmr;

    public  ElsaServerMongoTestConfiguration(DomainMetaRegistry dmr){
        this.dmr = dmr;
    }

    @Bean
    public ElsaServerMongoTestDomainMetaRegistryConfigurator elsaServerMongoTestDomainMetaRegistryConfigurator(){
        return new ElsaServerMongoTestDomainMetaRegistryConfigurator();
    }

    @Bean
    public MongoTestServer mongoTestServer(){
        return new MongoTestServer();
    }

    @Bean
    public TestMongoStorageFactory testMongoStorageFactory(){
        return new TestMongoStorageFactory();
    }
    @Bean
    public JdbcDatabaseCustomizer jdbcDatabaseCustomizer(){
        return new StandardMongoJdbcCustomizer(dmr);
    }
}
