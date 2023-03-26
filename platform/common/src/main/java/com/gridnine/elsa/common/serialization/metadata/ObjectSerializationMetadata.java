/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.serialization.metadata;

import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectSerializationMetadata {
    private final Map<String, PropertySerializationMetadata> allProperties = new LinkedHashMap<>();

    public Map<String, PropertySerializationMetadata> getAllProperties() {
        return allProperties;
    }
}
