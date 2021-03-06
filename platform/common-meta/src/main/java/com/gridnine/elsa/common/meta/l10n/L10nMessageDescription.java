/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.l10n;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class L10nMessageDescription extends BaseElementWitId {

    private final Map<Locale, String> displayNames = new LinkedHashMap<>();

    private final Map<String,L10nMessageParameterDescription> parameters = new LinkedHashMap<>();

    public L10nMessageDescription() {
    }

    public Map<Locale, String> getDisplayNames() {
        return displayNames;
    }

    public L10nMessageDescription(String id) {
        super(id);
    }

    public Map<String, L10nMessageParameterDescription> getParameters() {
        return parameters;
    }

}
