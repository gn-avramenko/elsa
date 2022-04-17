/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectMetadataProvidersFactory {

    private final Map<String, BaseObjectMetadataProvider<?>> providersCache = new ConcurrentHashMap<>();

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private CustomMetaRegistry customMetaRegistry;

    public BaseObjectMetadataProvider<?> getProvider(String className){
        BaseObjectMetadataProvider<?> result = providersCache.get(className);
        if(result != null){
            return result;
        }
        return providersCache.computeIfAbsent(className, this::createProvider);
    }

    private BaseObjectMetadataProvider<?> createProvider(String className)  {
        var docDescription = domainMetaRegistry.getDocuments().get(className);
        if (docDescription != null) {
            return new DomainDocumentMetadataProvider(docDescription, domainMetaRegistry,  customMetaRegistry);
        }
        var domainEntityDescription = domainMetaRegistry.getEntities().get(className);
        if (domainEntityDescription != null) {
            return new EntityMetadataProvider(domainEntityDescription, domainMetaRegistry,  customMetaRegistry);
        }
        var assetDescription = domainMetaRegistry.getAssets().get(className);
        if(assetDescription != null){
            return new AssetMetadataProvider(assetDescription);
        }
        var projectionDescription = domainMetaRegistry.getSearchableProjections().get(className);
        if(projectionDescription != null){
            return new SearchableProjectionMetadataProvider(projectionDescription);
        }
        var customEntityDescription = customMetaRegistry.getEntities().get(className);
        if (customEntityDescription != null) {
            return new EntityMetadataProvider(customEntityDescription, domainMetaRegistry,  customMetaRegistry);
        }
        throw Xeption.forDeveloper("no description found for entity %s".formatted(className));
    }

}
