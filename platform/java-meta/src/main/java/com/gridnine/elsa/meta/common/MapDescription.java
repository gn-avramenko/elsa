/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class MapDescription extends BaseElementWithId {

    private String keyGenericJavaType;

    private String valueGenericJavaType;
    private final Map<String,String> parameters = new LinkedHashMap<>();
    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    public MapDescription() {
    }

    public MapDescription(String id) {
        super(id);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

    public String getKeyGenericJavaType() {
        return keyGenericJavaType;
    }

    public void setKeyGenericJavaType(String keyGenericJavaType) {
        this.keyGenericJavaType = keyGenericJavaType;
    }

    public String getValueGenericJavaType() {
        return valueGenericJavaType;
    }

    public void setValueGenericJavaType(String valueGenericJavaType) {
        this.valueGenericJavaType = valueGenericJavaType;
    }
}
