/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

public class MapTypeDescription extends BaseFieldTypeDescription{

    private String keyGenericTypeName;

    private ValueType keyGenericType;

    private String valueGenericTypeName;

    private ValueType valueGenericType;


    public String getKeyGenericTypeName() {
        return keyGenericTypeName;
    }

    public void setKeyGenericTypeName(String keyGenericTypeName) {
        this.keyGenericTypeName = keyGenericTypeName;
    }

    public ValueType getKeyGenericType() {
        return keyGenericType;
    }

    public void setKeyGenericType(ValueType keyGenericType) {
        this.keyGenericType = keyGenericType;
    }

    public String getValueGenericTypeName() {
        return valueGenericTypeName;
    }

    public void setValueGenericTypeName(String valueGenericTypeName) {
        this.valueGenericTypeName = valueGenericTypeName;
    }

    public ValueType getValueGenericType() {
        return valueGenericType;
    }

    public void setValueGenericType(ValueType valueGenericType) {
        this.valueGenericType = valueGenericType;
    }
}
