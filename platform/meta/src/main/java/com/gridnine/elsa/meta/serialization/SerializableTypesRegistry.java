/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerializableTypesRegistry {

    private final Map<String, SerializableType> types = new LinkedHashMap<>();

    public Map<String, SerializableType> getTypes() {
        return types;
    }

    public static SerializableTypesRegistry get(){
        return Environment.getPublished(SerializableTypesRegistry.class);
    }
}
