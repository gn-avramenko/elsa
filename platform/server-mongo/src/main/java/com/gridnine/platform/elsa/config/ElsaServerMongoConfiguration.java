/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.config;

import com.gridnine.platform.elsa.common.mongo.ElsaServerMongoCustomMetaRegistryConfigurator;
import com.gridnine.platform.elsa.server.mongo.MongoFacade;
import com.gridnine.platform.elsa.server.mongo.MongoStorageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerMongoConfiguration {
    @Bean
    public MongoFacade mongoFacade(){
        return new MongoFacade();
    }
    @Bean
    public MongoStorageFactory mongoStorageFactory(){
        return new MongoStorageFactory();
    }


    @Bean
    public ElsaServerMongoCustomMetaRegistryConfigurator elsaServerMongoCustomMetaRegistryConfigurator(){
        return new ElsaServerMongoCustomMetaRegistryConfigurator();
    }
}
