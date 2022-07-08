/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiWidgetConfigurationDescription {

    private String className;

    private UiWidgetConfigurationPropertyType type;

    private final Map<String, UiWidgetConfigurationPropertyDescription> properties = new LinkedHashMap<>();

    public Map<String, UiWidgetConfigurationPropertyDescription> getProperties() {
        return properties;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public UiWidgetConfigurationPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetConfigurationPropertyType type) {
        this.type = type;
    }
}
