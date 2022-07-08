/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiWidgetValidationDescription {

    private String className;

    private UiWidgetValidationPropertyType type;

    private final Map<String, UiWidgetValidationPropertyDescription> properties = new LinkedHashMap<>();

    public Map<String, UiWidgetValidationPropertyDescription> getProperties() {
        return properties;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public UiWidgetValidationPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetValidationPropertyType type) {
        this.type = type;
    }
}
