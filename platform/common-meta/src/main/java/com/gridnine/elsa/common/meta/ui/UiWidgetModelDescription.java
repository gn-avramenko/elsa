/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiWidgetModelDescription {

    private String className;

    private UiWidgetModelPropertyType type;

    private final Map<String, UiWidgetModelPropertyDescription> properties = new LinkedHashMap<>();

    public String getClassName() {
        return className;
    }

    public Map<String, UiWidgetModelPropertyDescription> getProperties() {
        return properties;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public UiWidgetModelPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetModelPropertyType type) {
        this.type = type;
    }
}
