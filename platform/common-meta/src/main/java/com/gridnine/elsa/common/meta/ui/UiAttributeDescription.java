/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class UiAttributeDescription {
    private String name;
    private UiAttributeType type;
    private boolean nonNullable;
    private String className;
    private String defaultValue;

    public UiAttributeDescription() {
    }

    public UiAttributeDescription(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UiAttributeType getType() {
        return type;
    }

    public void setType(UiAttributeType type) {
        this.type = type;
    }

    public boolean isNonNullable() {
        return nonNullable;
    }

    public void setNonNullable(boolean nonNullable) {
        this.nonNullable = nonNullable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
