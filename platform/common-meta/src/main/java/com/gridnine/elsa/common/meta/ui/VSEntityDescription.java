/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class VSEntityDescription {
    private String extendsId;
    private final Map<String, VSPropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, VSCollectionDescription> collections = new LinkedHashMap<>();

    public Map<String, VSCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, VSPropertyDescription> getProperties() {
        return properties;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

    public String getExtendsId() {
        return extendsId;
    }

}
