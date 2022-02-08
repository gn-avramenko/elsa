/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VVCollectionDescription extends BaseModelElementDescription
{
    private VVCollectionType elementType;
    private String elementClassName;

    public VVCollectionType getElementType() {
        return elementType;
    }

    public void setElementType(VVCollectionType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }
}