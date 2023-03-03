/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.ArrayList;
import java.util.List;

public class AttributeDescription {
    private String name;
    private AttributeType type;
    private String defaultValue;
    private List<String> selectValues = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> getSelectValues() {
        return selectValues;
    }

    public void setSelectValues(List<String> selectValues) {
        this.selectValues = selectValues;
    }
}
