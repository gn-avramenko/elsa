/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.custom;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.domain.AssetDescription;
import com.gridnine.elsa.common.meta.domain.DocumentDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.domain.SearchableProjectionDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    @Autowired
    public CustomMetaRegistry(List<CustomMetaRegistryConfigurator> configurators) {
        configurators.forEach(it ->{
            it.updateMetaRegistry(this);
        });
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

}
