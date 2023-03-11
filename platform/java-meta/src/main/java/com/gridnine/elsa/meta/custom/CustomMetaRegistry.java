/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.custom;

import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashSet;
import java.util.Set;

public class CustomMetaRegistry {
    private final Set<String> enumsIds = new LinkedHashSet<>();

    private final Set<String> entitiesIds = new LinkedHashSet<>();

    public Set<String> getEnumsIds() {
        return enumsIds;
    }

    public Set<String> getEntitiesIds() {
        return entitiesIds;
    }

    public static CustomMetaRegistry get(){
        return Environment.getPublished(CustomMetaRegistry.class);
    }
}
