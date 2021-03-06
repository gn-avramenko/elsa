/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.custom;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CustomMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<CustomMetaRegistryConfigurator> configurators){
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

}
