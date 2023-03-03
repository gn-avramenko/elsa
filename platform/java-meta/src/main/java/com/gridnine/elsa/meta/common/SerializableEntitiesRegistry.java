/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerializableEntitiesRegistry {
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }
}
