/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class UiWidgetPropertiesDescription {
    private final Map<String, UiAttributeDescription> attributes = new LinkedHashMap<>();

    public Map<String, UiAttributeDescription> getAttributes() {
        return attributes;
    }
}
