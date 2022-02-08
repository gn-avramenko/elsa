/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

public class StandardCollectionDescription extends BaseModelElementDescription{

    private StandardValueType elementType;

    private String elementClassName;

    private boolean unique;

    public StandardCollectionDescription() {
    }

    public StandardCollectionDescription(String id) {
        setId(id);
    }
    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public StandardValueType getElementType() {
        return elementType;
    }

    public void setElementType(StandardValueType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }
}
