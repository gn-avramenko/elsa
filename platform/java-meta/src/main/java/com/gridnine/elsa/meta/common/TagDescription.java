/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TagDescription {

    private final Map<String, AttributeDescription> attributes = new LinkedHashMap<>();
    private String tagName;

    private String type;

    private String objectIdAttributeName;

    private final List<GenericDescription> generics = new ArrayList<>();

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public Map<String, AttributeDescription> getAttributes() {
        return attributes;
    }

    public List<GenericDescription> getGenerics() {
        return generics;
    }

    public String getObjectIdAttributeName() {
        return objectIdAttributeName;
    }

    public void setObjectIdAttributeName(String objectIdAttributeName) {
        this.objectIdAttributeName = objectIdAttributeName;
    }
}
