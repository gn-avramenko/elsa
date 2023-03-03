/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.ArrayList;
import java.util.List;

public class BaseFieldTypeDescription {

    private final List<AttributeDescription> attributes = new ArrayList<>();
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<AttributeDescription> getAttributes() {
        return attributes;
    }
}
