/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomWidgetRef extends BaseWidgetDescription{
    private String ref;
    private final Map<String,String> params = new LinkedHashMap<>();
    @Override
    public WidgetType getWidgetType() {
        return WidgetType.CUSTOM;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}
