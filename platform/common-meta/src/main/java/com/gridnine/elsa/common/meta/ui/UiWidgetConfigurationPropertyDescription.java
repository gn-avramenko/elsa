/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

public class UiWidgetConfigurationPropertyDescription extends BaseElementWithId {
    private UiWidgetConfigurationPropertyType type;
    private String className;
    private boolean nonNullable;

    public UiWidgetConfigurationPropertyDescription() {
    }

    public UiWidgetConfigurationPropertyDescription(String id) {
        super(id);
    }

    public UiWidgetConfigurationPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetConfigurationPropertyType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isNonNullable() {
        return nonNullable;
    }

    public void setNonNullable(boolean nonNullable) {
        this.nonNullable = nonNullable;
    }
}
