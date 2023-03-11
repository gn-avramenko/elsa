/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumDescription extends BaseElement {

    private final Map<String, EnumItemDescription> items = new LinkedHashMap<>();

    public EnumDescription() {
    }

    public EnumDescription(String id) {
        super(id);
    }

    public Map<String, EnumItemDescription> getItems() {
        return items;
    }
}
