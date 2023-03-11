/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityDescription extends BaseElement {

    private final Map<String, PropertyDescription> properties = new LinkedHashMap<>();

    public EntityDescription() {
    }

    public EntityDescription(String id) {
        super(id);
    }
    public Map<String, PropertyDescription> getProperties() {
        return properties;
    }

}
