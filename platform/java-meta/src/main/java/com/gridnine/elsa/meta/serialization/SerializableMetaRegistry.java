/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerializableMetaRegistry {
    private final Map<String, EntityDescription> entities = new LinkedHashMap<>();

    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();

    public Map<String, EntityDescription> getEntities() {
        return entities;
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public static SerializableMetaRegistry get(){
        return Environment.getPublished(SerializableMetaRegistry.class);
    }

}
