/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.rest;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();
    private final Map<String, RestDescription> rests = new LinkedHashMap<>();
    private final Map<String, RestGroupDescription> groups = new LinkedHashMap<>();
    private final Map<String, RestOperationDescription> operations = new LinkedHashMap<>();

    @Autowired
    public RestMetaRegistry(List<RestMetaRegistryConfigurator> configurators) {
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

    public Map<String, RestDescription> getRests() {
        return rests;
    }

    public Map<String, RestGroupDescription> getGroups() {
        return groups;
    }

    public Map<String, RestOperationDescription> getOperations() {
        return operations;
    }
}
