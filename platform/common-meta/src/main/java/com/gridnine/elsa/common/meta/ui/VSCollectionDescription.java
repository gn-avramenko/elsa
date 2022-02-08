/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class VSCollectionDescription extends BaseModelElementDescription
{
    private VSCollectionType elementType;
    private String elementClassName;

    public VSCollectionType getElementType() {
        return elementType;
    }

    public void setElementType(VSCollectionType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }
}
