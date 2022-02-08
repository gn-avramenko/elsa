/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class VMEntityDescription {
    private String extendsId;
    private final Map<String, VMPropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, VMCollectionDescription> collections = new LinkedHashMap<>();

    public Map<String, VMCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, VMPropertyDescription> getProperties() {
        return properties;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

    public String getExtendsId() {
        return extendsId;
    }

}
