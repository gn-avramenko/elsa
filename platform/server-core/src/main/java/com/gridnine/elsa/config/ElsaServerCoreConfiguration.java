/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.elsa.server.core.cache.CacheManager;
import com.gridnine.elsa.server.core.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.core.cache.ehCache.EhCacheManager;
import com.gridnine.elsa.server.core.codec.DesCodec;
import com.gridnine.elsa.server.core.storage.Storage;
import com.gridnine.elsa.server.core.storage.database.Database;
import com.gridnine.elsa.server.core.storage.database.DatabaseFactory;
import com.gridnine.elsa.server.core.storage.database.jdbc.model.JdbcDatabaseMetadataProvider;
import com.gridnine.elsa.server.core.storage.standard.CacheStorageAdvice;
import com.gridnine.elsa.server.core.storage.standard.IdUpdaterInterceptor;
import com.gridnine.elsa.server.core.storage.standard.InvalidateCacheStorageInterceptor;
import com.gridnine.elsa.server.core.storage.standard.StorageCaptionProviderImpl;
import com.gridnine.elsa.server.core.storage.transaction.ElsaTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ElsaServerCoreConfiguration {

    @Autowired
    private DatabaseFactory factory;

    @Bean
    public DesCodec desCodec(){
        return new DesCodec();
    }

    @Bean
    public Storage storage(){
        return new Storage();
    }

    @Bean
    public  Database primaryDatabase(){
        return factory.getPrimaryDatabase();
    }

    @Bean
    public  ClassMapper classMapper(){
        return factory.getClassMapper();
    }

    @Bean
    public  EnumMapper enumMapper(){
        return factory.getEnumMapper();
    }

    @Bean
    public  IdGenerator idGenerator(){
        return factory.getIdGenerator();
    }

    @Bean
    public  PlatformTransactionManager transactionManager(){
        return factory.getTransactionManager();
    }

    @Bean
    public ElsaTransactionManager elsaTransactionManager(){
        return new ElsaTransactionManager();
    }

    @Bean
    public JdbcDatabaseMetadataProvider databaseMetadataProvider(){
        return new JdbcDatabaseMetadataProvider();
    }

    @Bean
    public CacheManager standardCacheManager(){
        return new EhCacheManager();
    }

    @Bean
    public CacheStorageAdvice cacheStorageAdvice(){
        return new CacheStorageAdvice();
    }

    @Bean
    public IdUpdaterInterceptor idUpdaterInterceptor(){
        return new IdUpdaterInterceptor();
    }

    @Bean
    public InvalidateCacheStorageInterceptor invalidateCacheStorageInterceptor(){
        return new InvalidateCacheStorageInterceptor();
    }

    @Bean
    public CaptionProvider captionProvider(){
        return new StorageCaptionProviderImpl();
    }

    @Bean
    public CacheMetadataProvider cacheMetadataProvider(){
        return new CacheMetadataProvider();
    }
}
