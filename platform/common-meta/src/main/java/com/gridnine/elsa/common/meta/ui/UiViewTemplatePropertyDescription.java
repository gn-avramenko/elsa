/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiViewTemplatePropertyDescription {
    private String tagName;
    private UiViewTemplatePropertyType type;
    private boolean nonNullable;
    private String className;
    private final Map<String, UiAttributeDescription> attributes = new LinkedHashMap<>();
    private final Map<String, UiViewTemplatePropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, UiViewTemplateCollectionDescription> collections = new LinkedHashMap<>();
    private final List<UiGroupDescription> groups = new ArrayList<>();

    public UiViewTemplatePropertyDescription() {
    }

    public UiViewTemplatePropertyDescription(String tagName) {
        this.tagName = tagName;
    }


    public UiViewTemplatePropertyType getType() {
        return type;
    }

    public void setType(UiViewTemplatePropertyType type) {
        this.type = type;
    }

    public boolean isNonNullable() {
        return nonNullable;
    }

    public void setNonNullable(boolean nonNullable) {
        this.nonNullable = nonNullable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, UiViewTemplatePropertyDescription> getProperties() {
        return properties;
    }

    public Map<String, UiViewTemplateCollectionDescription> getCollections() {
        return collections;
    }

    public Map<String, UiAttributeDescription> getAttributes() {
        return attributes;
    }

    public List<UiGroupDescription> getGroups() {
        return groups;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}

