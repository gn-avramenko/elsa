/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.dataTransfer;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DtMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    @Autowired
    public DtMetaRegistry(List<DtMetaRegistryConfigurator> configurators) {
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
