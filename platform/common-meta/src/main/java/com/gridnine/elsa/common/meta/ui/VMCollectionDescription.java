/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VMCollectionDescription extends BaseModelElementDescription
{
    private VMCollectionType elementType;
    private String elementClassName;

    public VMCollectionType getElementType() {
        return elementType;
    }

    public void setElementType(VMCollectionType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }
}
