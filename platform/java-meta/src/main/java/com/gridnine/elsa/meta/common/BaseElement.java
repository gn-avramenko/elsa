/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseElement extends BaseElementWithId{
    private final Map<String,String> attributes = new LinkedHashMap<>();

    private final Map<String,String> parameters = new LinkedHashMap<>();

    private final Map<Locale, String> displayNames = new LinkedHashMap<>();


    public BaseElement() {
    }

    public BaseElement(String id) {
        super(id);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }
}
