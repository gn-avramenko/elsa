/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.domain;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.CollectionTypeDescription;
import com.gridnine.elsa.meta.common.MapTypeDescription;
import com.gridnine.elsa.meta.common.PropertyTypeDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class DomainTypesRegistry {
    private Map<String, AttributeDescription> documentAttributes = new LinkedHashMap<>();
    private Map<String, AttributeDescription> projectionAttributes = new LinkedHashMap<>();
    private Map<String, AttributeDescription> assetAttributes = new LinkedHashMap<>();
    private Map<String, PropertyTypeDescription> entityProperties = new LinkedHashMap<>();
    private Map<String, CollectionTypeDescription> entityCollections = new LinkedHashMap<>();
    private Map<String, MapTypeDescription> entityMaps = new LinkedHashMap<>();
    private Map<String, PropertyTypeDescription> databaseProperties = new LinkedHashMap<>();
    private Map<String, CollectionTypeDescription> databaseCollections = new LinkedHashMap<>();

    public Map<String, AttributeDescription> getDocumentAttributes() {
        return documentAttributes;
    }

    public void setDocumentAttributes(Map<String, AttributeDescription> documentAttributes) {
        this.documentAttributes = documentAttributes;
    }

    public Map<String, AttributeDescription> getProjectionAttributes() {
        return projectionAttributes;
    }

    public void setProjectionAttributes(Map<String, AttributeDescription> projectionAttributes) {
        this.projectionAttributes = projectionAttributes;
    }

    public Map<String, AttributeDescription> getAssetAttributes() {
        return assetAttributes;
    }

    public void setAssetAttributes(Map<String, AttributeDescription> assetAttributes) {
        this.assetAttributes = assetAttributes;
    }

    public Map<String, PropertyTypeDescription> getEntityProperties() {
        return entityProperties;
    }

    public void setEntityProperties(Map<String, PropertyTypeDescription> entityProperties) {
        this.entityProperties = entityProperties;
    }

    public Map<String, CollectionTypeDescription> getEntityCollections() {
        return entityCollections;
    }

    public void setEntityCollections(Map<String, CollectionTypeDescription> entityCollections) {
        this.entityCollections = entityCollections;
    }

    public Map<String, MapTypeDescription> getEntityMaps() {
        return entityMaps;
    }

    public void setEntityMaps(Map<String, MapTypeDescription> entityMaps) {
        this.entityMaps = entityMaps;
    }

    public Map<String, PropertyTypeDescription> getDatabaseProperties() {
        return databaseProperties;
    }

    public void setDatabaseProperties(Map<String, PropertyTypeDescription> databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    public Map<String, CollectionTypeDescription> getDatabaseCollections() {
        return databaseCollections;
    }

    public void setDatabaseCollections(Map<String, CollectionTypeDescription> databaseCollections) {
        this.databaseCollections = databaseCollections;
    }
}
