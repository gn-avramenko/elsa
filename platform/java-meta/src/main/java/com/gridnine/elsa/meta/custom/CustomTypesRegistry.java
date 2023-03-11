/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.custom;

import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTypesRegistry {

    private final  Map<String, TagDescription> entityTags = new LinkedHashMap<>();

    public Map<String, TagDescription> getEntityTags() {
        return entityTags;
    }

    public static CustomTypesRegistry get(){
        return Environment.getPublished(CustomTypesRegistry.class);
    }
}
