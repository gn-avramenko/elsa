/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VMPropertyDescription extends BaseModelElementDescription {

    private  VMPropertyType type;
    private String className;
    private boolean nullable;

    public VMPropertyType getType() {
        return type;
    }

    public void setType(VMPropertyType type) {
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
