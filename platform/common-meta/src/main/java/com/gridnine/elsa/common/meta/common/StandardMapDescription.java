/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

public class StandardMapDescription extends BaseModelElementDescription{
    private StandardValueType keyType;

    private StandardValueType valueType;

    private String keyClassName;

    private String valueClassName;

    private boolean unique;

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
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
