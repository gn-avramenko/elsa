/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VVPropertyDescription extends BaseModelElementDescription {

    private  VVPropertyType type;
    private String className;

    public VVPropertyType getType() {
        return type;
    }

    public void setType(VVPropertyType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
