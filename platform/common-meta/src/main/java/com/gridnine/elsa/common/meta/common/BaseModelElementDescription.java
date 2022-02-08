/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseModelElementDescription {
    private String id;
    private final Map<String,String> parameters = new LinkedHashMap<>();
    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

}
