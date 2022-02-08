/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

public class StandardPropertyDescription extends BaseModelElementDescription{
    private StandardValueType type;

    private String className;

    private boolean nullable;

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

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
