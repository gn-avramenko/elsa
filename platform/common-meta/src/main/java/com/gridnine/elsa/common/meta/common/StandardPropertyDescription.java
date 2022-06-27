/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

public class StandardPropertyDescription extends BaseModelElementDescription{

    public StandardPropertyDescription() {
    }

    public StandardPropertyDescription(String id) {
        setId(id);
    }

    private StandardValueType type;

    private String className;

    private boolean nonNullable;

    public StandardValueType getType() {
        return type;
    }

    public void setType(StandardValueType type) {
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
