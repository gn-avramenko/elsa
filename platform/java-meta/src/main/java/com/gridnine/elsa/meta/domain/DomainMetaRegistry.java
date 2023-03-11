/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.domain;

import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashSet;
import java.util.Set;

public class DomainMetaRegistry {
    private final Set<String> enumsIds = new LinkedHashSet<>();

    private final Set<String> documentsIds = new LinkedHashSet<>();

    private final Set<String> entitiesIds = new LinkedHashSet<>();

    private final Set<String> projectionsIds = new LinkedHashSet<>();

    private final Set<String> assetsIds = new LinkedHashSet<>();

    public Set<String> getEnumsIds() {
        return enumsIds;
    }

    public Set<String> getDocumentsIds() {
        return documentsIds;
    }

    public Set<String> getEntitiesIds() {
        return entitiesIds;
    }

    public Set<String> getProjectionsIds() {
        return projectionsIds;
    }

    public Set<String> getAssetsIds() {
        return assetsIds;
    }


    public static DomainMetaRegistry get(){
        return Environment.getPublished(DomainMetaRegistry.class);
    }
}
