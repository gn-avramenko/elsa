/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumDescription extends BaseModelElementDescription{
    public EnumDescription() {
    }

    public EnumDescription(String id) {
        setId(id);
    }

    private final Map<String, EnumItemDescription> items = new LinkedHashMap<>();

    public Map<String, EnumItemDescription> getItems() {
        return items;
    }
}
