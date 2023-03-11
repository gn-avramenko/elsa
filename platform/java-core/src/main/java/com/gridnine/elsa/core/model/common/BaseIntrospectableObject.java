/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

public abstract class BaseIntrospectableObject {

    public Object getValue(String propertyName) {
        throw new IllegalArgumentException("no property with id %s".formatted(propertyName));
    }

    public void setValue(String propertyName, Object value) {
        throw new IllegalArgumentException("no property with id %s".formatted(propertyName));
    }
}
