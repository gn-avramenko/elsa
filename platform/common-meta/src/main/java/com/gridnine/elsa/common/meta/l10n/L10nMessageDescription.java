/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.l10n;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class L10nMessageDescription {
    private String id;

    private final Map<String,L10nMessageParameterDescription> parameters = new LinkedHashMap<>();

    public Map<String, L10nMessageParameterDescription> getParameters() {
        return parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
