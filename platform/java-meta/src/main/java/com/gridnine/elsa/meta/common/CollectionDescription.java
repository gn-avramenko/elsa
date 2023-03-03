/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class CollectionDescription extends BaseElementWithId {

    private boolean unique;

    private String genericJavaType;
    private final Map<String,String> parameters = new LinkedHashMap<>();
    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    public CollectionDescription() {
    }

    public CollectionDescription(String id) {
        super(id);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

    public String getGenericJavaType() {
        return genericJavaType;
    }

    public void setGenericJavaType(String genericJavaType) {
        this.genericJavaType = genericJavaType;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
}
