/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.IdGenerator;
import com.gridnine.elsa.server.core.cache.CacheManager;
import com.gridnine.elsa.server.core.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.core.cache.ehCache.EhCacheManager;
import com.gridnine.elsa.server.core.encrypt.DesCodec;
import com.gridnine.elsa.server.core.storage.Database;
import com.gridnine.elsa.server.core.storage.Storage;
import com.gridnine.elsa.server.core.storage.StorageInterceptor;
import com.gridnine.elsa.server.core.storage.jdbc.*;
import com.gridnine.elsa.server.core.storage.jdbc.model.DatabaseMetadataProvider;
import com.gridnine.elsa.server.core.storage.jdbc.structureUpdater.JdbcStructureUpdater;
import com.gridnine.elsa.server.core.storage.standard.CacheStorageAdvice;
import com.gridnine.elsa.server.core.storage.standard.IdUpdaterInterceptor;
import com.gridnine.elsa.server.core.storage.standard.InvalidateCacheStorageInterceptor;
import com.gridnine.elsa.server.core.storage.transaction.TransactionManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ElsaServerCoreTestConfiguration {

    @Bean
    public DesCodec desCodec() {
        return new DesCodec();
    }

    @Bean
    public DataSourceProvider hsqldbProvider() {
        return new HsqldbDataSourceProvider();
    }

    @Bean
    public DatabaseMetadataProvider databaseMetadataProvider() {
        return new DatabaseMetadataProvider();
    }

    @Bean
    public JdbcStructureUpdater structureUpdater() {
        return new JdbcStructureUpdater();
    }

    @Bean
    public ClassMapper classMapper(){
        return new JdbcClassMapperImpl();
    }

    @Bean
    public EnumMapper enumMapper(){
        return new JdbcEnumMapperImpl();
    }

    @Bean
    public TransactionManager transactionManager(){
        return new TransactionManager();
    }

    @Bean
    public Storage storage(){
        return new Storage();
    }

    @Bean
    public Database database(){
        return new JdbcDatabase();
    }

    @Bean
    public StorageInterceptor idInterceptor(){
        return new IdUpdaterInterceptor();
    }

    @Bean
    public IdGenerator idGenerator(){
        return new JdbcIdGeneratorImpl();
    }

    @Bean
    public TestDomainDocumentProjectionHandler testDomainDocumentProjectionHandler(){
        return new TestDomainDocumentProjectionHandler();
    }

    @Bean
    public CacheStorageAdvice cacheStorageAdvice(){
        return new CacheStorageAdvice();
    }

    @Bean
    public InvalidateCacheStorageInterceptor invalidateCacheStorageInterceptor(){
        return new InvalidateCacheStorageInterceptor();
    }

    @Bean
    public CacheMetadataProvider cacheMetadataProvider(){
        return new CacheMetadataProvider();
    }

    @Bean
    public CacheManager cacheManager(){
        return new EhCacheManager();
    }
}
