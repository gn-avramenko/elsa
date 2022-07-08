/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

public class UiWidgetModelPropertyDescription extends BaseElementWithId {
    private UiWidgetModelPropertyType type;
    private String className;
    private boolean nonNullable;

    public UiWidgetModelPropertyDescription() {
    }

    public UiWidgetModelPropertyDescription(String id) {
        super(id);
    }

    public UiWidgetModelPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetModelPropertyType type) {
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
