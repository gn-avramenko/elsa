/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.ModuleImportDescription;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RemotingMetaRegistry {
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();

    private final Map<String, RemotingDescription> remotings = new LinkedHashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<RemotingMetaRegistryConfigurator> configurators){
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }


    public Map<String, RemotingDescription> getRemotings() {
        return remotings;
    }
}
