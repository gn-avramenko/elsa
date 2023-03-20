/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.activator;

import com.gridnine.elsa.core.CoreDomainTypesConfigurator;
import com.gridnine.elsa.core.config.Activator;
import com.gridnine.elsa.core.model.common.CaptionProvider;
import com.gridnine.elsa.core.model.common.EnumMapper;
import com.gridnine.elsa.core.model.common.IdGenerator;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.server.ServerL10nMessagesRegistryConfigurator;
import com.gridnine.elsa.server.cache.CacheManager;
import com.gridnine.elsa.server.cache.CacheMetadataProvider;
import com.gridnine.elsa.server.cache.ehCache.EhCacheManager;
import com.gridnine.elsa.server.codec.DesCodec;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.storage.StorageRegistry;
import com.gridnine.elsa.server.storage.repository.Repository;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcEnumMapperImpl;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcIdGeneratorImpl;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcRepository;
import com.gridnine.elsa.server.storage.repository.jdbc.JdbcTranscationManager;
import com.gridnine.elsa.server.storage.repository.jdbc.handlers.*;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcDatabaseMetadataProvider;
import com.gridnine.elsa.server.storage.repository.jdbc.structureUpdater.JdbcStructureUpdater;
import com.gridnine.elsa.server.storage.standard.CacheStorageAdvice;
import com.gridnine.elsa.server.storage.standard.IdUpdaterInterceptor;
import com.gridnine.elsa.server.storage.standard.InvalidateCacheStorageInterceptor;
import com.gridnine.elsa.server.storage.standard.StorageCaptionProviderImpl;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;

public class ServerActivator implements Activator {
    @Override
    public double getOrder() {
        return 3;
    }

    @Override
    public void configure() throws Exception {
        Environment.publish(new CacheMetadataProvider());
        Environment.publish(CacheManager.class, new EhCacheManager());
        Environment.publish(new DesCodec());
        Environment.publish(new JdbcRegistry());
        new ServerL10nMessagesRegistryConfigurator().configure();
        JdbcRegistry.get().register(SqlTypeBigDecimalHandler.type, new SqlTypeBigDecimalHandler());
        JdbcRegistry.get().register(SqlTypeBlobHandler.type, new SqlTypeBlobHandler());
        JdbcRegistry.get().register(SqlTypeBooleanHandler.type, new SqlTypeBooleanHandler());
        JdbcRegistry.get().register(SqlTypeDateHandler.type, new SqlTypeDateHandler());
        JdbcRegistry.get().register(SqlTypeIntArrayHandler.type, new SqlTypeIntArrayHandler());
        JdbcRegistry.get().register(SqlTypeIntHandler.type, new SqlTypeIntHandler());
        JdbcRegistry.get().register(SqlTypeIntIdHandler.type, new SqlTypeIntIdHandler());
        JdbcRegistry.get().register(SqlTypeLongArrayHandler.type, new SqlTypeLongArrayHandler());
        JdbcRegistry.get().register(SqlTypeLongHandler.type, new SqlTypeLongHandler());
        JdbcRegistry.get().register(SqlTypeLongIdHandler.type, new SqlTypeLongIdHandler());
        JdbcRegistry.get().register(SqlTypeStringArrayHandler.type, new SqlTypeStringArrayHandler());
        JdbcRegistry.get().register(SqlTypeStringHandler.type, new SqlTypeStringHandler());
        JdbcRegistry.get().register(SqlTypeTextHandler.type, new SqlTypeTextHandler());
        JdbcRegistry.get().register(SqlTypeTimestampHandler.type, new SqlTypeTimestampHandler());

        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_BIG_DECIMAL_PROPERTY, new JdbcBigDecimalFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_BOOLEAN_PROPERTY, new JdbcBooleanFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_ENTITY_REFERENCE_LIST, new JdbcEntityReferenceCollectionFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_ENTITY_REFERENCE_PROPERTY, new JdbcEntityReferenceFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_ENUM_LIST, new JdbcEnumCollectionFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_ENUM_PROPERTY, new JdbcEnumFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_LOCAL_DATE_PROPERTY, new JdbcLocalDateFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_LOCAL_DATE_TIME_PROPERTY, new JdbcLocalDateTimeFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_LONG_PROPERTY, new JdbcLongFieldHandlerFactory());
        JdbcRegistry.get().register(CoreDomainTypesConfigurator.TAG_STRING_PROPERTY, new JdbcStringFieldHandlerFactory());

        Environment.publish(TransactionManager.class, new JdbcTranscationManager());

        Environment.publish(new StorageRegistry());
        CacheStorageAdvice cacheStorageAdvice = new CacheStorageAdvice();
        StorageRegistry.get().register(cacheStorageAdvice);
        var captionProvider = new StorageCaptionProviderImpl();
        Environment.publish(CaptionProvider.class, captionProvider);
        StorageRegistry.get().register(new InvalidateCacheStorageInterceptor(cacheStorageAdvice, captionProvider));
        StorageRegistry.get().register(new IdUpdaterInterceptor());
    }

    @Override
    public void activate() throws Exception {
        Environment.publish(new JdbcDatabaseMetadataProvider());
        JdbcStructureUpdater.updateStructure();
        Environment.publish(EnumMapper.class, new JdbcEnumMapperImpl());
        Environment.publish(IdGenerator.class, new JdbcIdGeneratorImpl());
        Environment.publish(Repository.class, new JdbcRepository());
        Environment.publish(new Storage());
    }
}
