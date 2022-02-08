/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class VVEntityDescription {
    private String extendsId;
    private final Map<String, VVPropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, VVCollectionDescription> collections = new LinkedHashMap<>();

    public Map<String, VVCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, VVPropertyDescription> getProperties() {
        return properties;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

    public String getExtendsId() {
        return extendsId;
    }

}
