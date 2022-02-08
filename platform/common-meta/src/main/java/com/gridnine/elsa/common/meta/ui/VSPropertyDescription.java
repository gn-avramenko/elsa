/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VSPropertyDescription extends BaseModelElementDescription {

    private  VSPropertyType type;
    private String className;

    public VSPropertyType getType() {
        return type;
    }

    public void setType(VSPropertyType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
