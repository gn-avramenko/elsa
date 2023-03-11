/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.l10n;

import com.gridnine.elsa.meta.common.BaseElementWithId;
import com.gridnine.elsa.meta.common.PropertyDescription;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class L10nMessageDescription extends BaseElementWithId {

    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    private final Map<String, PropertyDescription> parameters = new LinkedHashMap<>();

    private final Map<String,String> attributes = new LinkedHashMap<>();

    public L10nMessageDescription() {
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

    public L10nMessageDescription(String id) {
        super(id);
    }

    public Map<String, PropertyDescription> getParameters() {
        return parameters;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
