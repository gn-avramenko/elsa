/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

import java.util.Collection;
import java.util.Map;

public abstract class BaseIntrospectableObject {

    public Object getValue(String propertyName) {
        throw new IllegalArgumentException("no property with id %s".formatted(propertyName));
    }

    public Collection<?> getCollection(String collectionName) {
        throw new IllegalArgumentException("no collection with id %s".formatted(collectionName));
    }

    public Map<?, ?> getMap(String mapName) {
        throw new IllegalArgumentException("no map with id %s".formatted(mapName));
    }

    public void setValue(String propertyName, Object value) {
        throw new IllegalArgumentException("no property with id %s".formatted(propertyName));
    }
}
