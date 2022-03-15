/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DomainMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String,DocumentDescription> documents = new LinkedHashMap<>();
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();
    private final Map<String,SearchableProjectionDescription> searchableProjections = new LinkedHashMap<>();
    private final Map<String,AssetDescription> assets = new LinkedHashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<DomainMetaRegistryConfigurator> configurators){
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public Map<String, DocumentDescription> getDocuments() {
        return documents;
    }

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

    public Map<String, SearchableProjectionDescription> getSearchableProjections() {
        return searchableProjections;
    }

    public Map<String, AssetDescription> getAssets() {
        return assets;
    }
}
