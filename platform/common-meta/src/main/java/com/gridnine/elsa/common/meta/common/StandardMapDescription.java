/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

public class StandardMapDescription extends BaseElementWitId{
    private StandardValueType keyType;

    private StandardValueType valueType;

    private String keyClassName;

    private String valueClassName;

    public StandardMapDescription() {
    }

    public StandardMapDescription(String id) {
        super(id);
    }

    public StandardValueType getKeyType() {
        return keyType;
    }

    public void setKeyType(StandardValueType keyType) {
        this.keyType = keyType;
    }

    public String getKeyClassName() {
        return keyClassName;
    }

    public void setKeyClassName(String keyClassName) {
        this.keyClassName = keyClassName;
    }

    public StandardValueType getValueType() {
        return valueType;
    }

    public void setValueType(StandardValueType valueType) {
        this.valueType = valueType;
    }

    public String getValueClassName() {
        return valueClassName;
    }

    public void setValueClassName(String valueClassName) {
        this.valueClassName = valueClassName;
    }
}
