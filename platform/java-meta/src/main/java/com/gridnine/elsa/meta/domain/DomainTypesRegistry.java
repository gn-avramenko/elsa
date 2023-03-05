/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.domain;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.TagDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class DomainTypesRegistry {
    private final Map<String, AttributeDescription> documentAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> projectionAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> assetAttributes = new LinkedHashMap<>();

    private final Map<String, AttributeDescription> enumAttributes = new LinkedHashMap<>();

    private final Map<String, AttributeDescription> enumItemAttributes = new LinkedHashMap<>();

    private final  Map<String, TagDescription> entityTags = new LinkedHashMap<>();

    private final  Map<String, TagDescription> databaseTags = new LinkedHashMap<>();

    public Map<String, AttributeDescription> getDocumentAttributes() {
        return documentAttributes;
    }

    public Map<String, AttributeDescription> getProjectionAttributes() {
        return projectionAttributes;
    }

    public Map<String, AttributeDescription> getAssetAttributes() {
        return assetAttributes;
    }


    public Map<String, TagDescription> getEntityTags() {
        return entityTags;
    }

    public Map<String, TagDescription> getDatabaseTags() {
        return databaseTags;
    }

    public Map<String, AttributeDescription> getEnumAttributes() {
        return enumAttributes;
    }

    public Map<String, AttributeDescription> getEnumItemAttributes() {
        return enumItemAttributes;
    }
}
