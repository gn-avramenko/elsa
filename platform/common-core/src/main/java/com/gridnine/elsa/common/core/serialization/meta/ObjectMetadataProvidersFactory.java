/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.serialization.meta;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.dataTransfer.DataTransferMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ObjectMetadataProvidersFactory {

    private final Map<String, BaseObjectMetadataProvider<?>> providersCache = new ConcurrentHashMap<>();

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private RestMetaRegistry restMetaRegistry;

    @Autowired
    private DataTransferMetaRegistry dtMetaRegistry;

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
            return new DomainDocumentMetadataProvider(docDescription, domainMetaRegistry, restMetaRegistry, dtMetaRegistry, customMetaRegistry);
        }
        var domainEntityDescription = domainMetaRegistry.getEntities().get(className);
        if (domainEntityDescription != null) {
            return new EntityMetadataProvider(domainEntityDescription, domainMetaRegistry, restMetaRegistry, dtMetaRegistry, customMetaRegistry);
        }
        var assetDescription = domainMetaRegistry.getAssets().get(className);
        if(assetDescription != null){
            return new AssetMetadataProvider(assetDescription);
        }
        var projectionDescription = domainMetaRegistry.getSearchableProjections().get(className);
        if(projectionDescription != null){
            return new SearchableProjectionMetadataProvider(projectionDescription);
        }
        var restEntityDescription = restMetaRegistry.getEntities().get(className);
        if (restEntityDescription != null) {
            return new EntityMetadataProvider(restEntityDescription, domainMetaRegistry, restMetaRegistry, dtMetaRegistry, customMetaRegistry);
        }
        var customEntityDescription = customMetaRegistry.getEntities().get(className);
        if (customEntityDescription != null) {
            return new EntityMetadataProvider(customEntityDescription, domainMetaRegistry, restMetaRegistry, dtMetaRegistry, customMetaRegistry);
        }
        throw Xeption.forDeveloper("no description found for entity %s".formatted(className));
    }

}
