/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiViewTemplateCollectionDescription {
    private String elementTagName;
    private UiViewTemplatePropertyType elementType;
    private String elementClassName;
    private String wrapperTagName;
    private final Map<String, UiAttributeDescription> attributes = new LinkedHashMap<>();
    private final Map<String, UiViewTemplatePropertyDescription> properties = new LinkedHashMap<>();
    private final Map<String, UiViewTemplateCollectionDescription> collections = new LinkedHashMap<>();
    private final List<UiRefDescription> groups = new ArrayList<>();

    public UiViewTemplateCollectionDescription() {
    }

    public UiViewTemplateCollectionDescription(String elementTagName) {
        this.elementTagName = elementTagName;
    }

    public String getElementTagName() {
        return elementTagName;
    }

    public void setElementTagName(String elementTagName) {
        this.elementTagName = elementTagName;
    }

    public UiViewTemplatePropertyType getElementType() {
        return elementType;
    }

    public void setElementType(UiViewTemplatePropertyType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }

    public List<UiRefDescription> getGroups() {
        return groups;
    }


    public Map<String, UiAttributeDescription> getAttributes() {
        return attributes;
    }

    public Map<String, UiViewTemplatePropertyDescription> getProperties() {
        return properties;
    }

    public Map<String, UiViewTemplateCollectionDescription> getCollections() {
        return collections;
    }

    public String getWrapperTagName() {
        return wrapperTagName;
    }

    public void setWrapperTagName(String wrapperTagName) {
        this.wrapperTagName = wrapperTagName;
    }
}
