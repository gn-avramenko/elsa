/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

public class CollectionTypeDescription extends BaseFieldTypeDescription{

    private String genericTypeName;

    private ValueType genericType;

    public String getGenericTypeName() {
        return genericTypeName;
    }

    public void setGenericTypeName(String genericTypeName) {
        this.genericTypeName = genericTypeName;
    }

    public ValueType getGenericType() {
        return genericType;
    }

    public void setGenericType(ValueType genericType) {
        this.genericType = genericType;
    }
}
