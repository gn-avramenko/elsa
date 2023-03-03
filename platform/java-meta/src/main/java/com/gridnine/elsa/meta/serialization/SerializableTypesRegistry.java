/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerializableTypesRegistry {
    private final Map<String, SerializableType> types = new LinkedHashMap<>();

    public Map<String, SerializableType> getTypes() {
        return types;
    }
}
