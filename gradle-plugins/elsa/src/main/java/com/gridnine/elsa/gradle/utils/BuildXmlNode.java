/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildXmlNode
{
    private  String name;

    private String value;
    private final List<BuildXmlNode> children = new ArrayList<>();
    private final Map<String,String> attributes = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BuildXmlNode> getChildren() {
        return children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public List<BuildXmlNode> getChildren(String name) {
        return children.stream().filter(it -> it.name.equals(name)).toList();
    }

    public BuildXmlNode getFirstChild(String name) {
        return children.stream().filter(it -> it.name.equals(name)).findFirst().orElse(null);
    }

    public String getAttribute(String name){
        return attributes.get(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "XmlNode{" +
                "name='" + name + '\'' +
                "attributes='" + attributes + '\'' +
                "value='" + value + '\'' +
                '}';
    }
}
