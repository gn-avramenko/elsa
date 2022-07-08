/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class UiViewTemplateContent {
    private final Map<String, UiViewTemplatePropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, UiViewTemplateCollectionDescription> collections = new LinkedHashMap<>();

    public Map<String, UiViewTemplateCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, UiViewTemplatePropertyDescription> getProperties() {
        return properties;
    }
}
