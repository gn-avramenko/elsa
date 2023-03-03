/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class EntityDescription extends BaseElementWithId {

    private final Map<String,String> parameters = new LinkedHashMap<>();
    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    private final Map<String, PropertyDescription> properties = new LinkedHashMap<>();

    private final Map<String, CollectionDescription> collections = new LinkedHashMap<>();

    private final Map<String, MapDescription> maps = new LinkedHashMap<>();

    public EntityDescription() {
    }

    public EntityDescription(String id) {
        super(id);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

    public Map<String, PropertyDescription> getProperties() {
        return properties;
    }

    public Map<String, CollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, MapDescription> getMaps() {
        return maps;
    }
}
