/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

public class UiWidgetValidationPropertyDescription extends BaseElementWithId {
    private UiWidgetValidationPropertyType type;
    private String className;

    public UiWidgetValidationPropertyDescription() {
    }

    public UiWidgetValidationPropertyDescription(String id) {
        super(id);
    }

    public UiWidgetValidationPropertyType getType() {
        return type;
    }

    public void setType(UiWidgetValidationPropertyType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
