/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;

import com.gridnine.platform.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.platform.elsa.common.core.lock.LockManager;
import com.gridnine.platform.elsa.common.core.model.common.ClassMapper;
import com.gridnine.platform.elsa.common.core.model.common.EnumMapper;
import com.gridnine.platform.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.cache.CacheManager;
import com.gridnine.platform.elsa.core.cache.CacheMetadataProvider;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.core.storage.StorageFactory;
import com.gridnine.platform.elsa.core.storage.transaction.ElsaTransactionManager;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

public class MongoStorageFactory implements StorageFactory {

    @Autowired
    private AbstractBeanFactory abstractBeanFactory;

    @Autowired
    private ListableBeanFactory factory;

    @Autowired
    private LockManager lockManager;

    @Autowired
    private DomainMetaRegistry metaRegistry;

    @Autowired
    private SupportedLocalesProvider localesProvider;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private MongoFacade mongoFacade;

    @Autowired(required = false)
    private MongoStorageCustomizer mongoStorageCustomizer;

    private MongoStorage storage;

    private CaptionProvider captionProvider;

    @Autowired
    private ObjectMetadataProvidersFactory metadataProvidersFactory;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    private Environment env;

    @Autowired
    private CacheMetadataProvider cacheMetadataProvider;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public String getId() {
        return "MONGO";
    }

    @Override
    public void init(String prefix, Map<String, Object> customParameters){
        customParameters.put(MongoStorageCustomizer.KEY, mongoStorageCustomizer);
        String url = abstractBeanFactory.resolveEmbeddedValue("${%s.url}".formatted(prefix));
        String db = abstractBeanFactory.resolveEmbeddedValue("${%s.db}".formatted(prefix));
        var settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(url))
                .uuidRepresentation(UuidRepresentation.STANDARD).build();
        var client = MongoClients.create(settings);
        var template = new MongoTemplate(client, db);
        captionProvider = new MongoCaptionProviderImpl(env, cacheMetadataProvider,  template, domainMetaRegistry, this.cacheManager,localesProvider);
        storage = new MongoStorage(factory, lockManager, domainMetaRegistry, localesProvider, template, captionProvider,
                abstractBeanFactory, mongoFacade, metadataProvidersFactory, reflectionFactory,  customParameters);
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public ClassMapper getClassMapper() {
        return storage.getClassMapper();
    }

    @Override
    public EnumMapper getEnumMapper() {
        return storage.getEnumMapper();
    }

    @Override
    public ElsaTransactionManager getTransactionManager() {
        return storage.getTransactionManager();
    }

    @Override
    public CaptionProvider getCaptionProvider() {
        return captionProvider;
    }
}
