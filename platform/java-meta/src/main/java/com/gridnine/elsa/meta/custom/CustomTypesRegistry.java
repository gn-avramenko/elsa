/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.custom;

import com.gridnine.elsa.meta.common.TagDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTypesRegistry {

    private final  Map<String, TagDescription> entityTags = new LinkedHashMap<>();

    public Map<String, TagDescription> getEntityTags() {
        return entityTags;
    }
}
