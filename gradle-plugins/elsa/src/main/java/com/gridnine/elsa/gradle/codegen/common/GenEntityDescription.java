/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;


import java.util.LinkedHashMap;
import java.util.Map;

public class GenEntityDescription {
    private String toStringExpression;
    private String toLocalizableStringExpression;

    private String id;

    private String extendsId;

    private boolean isAbstract;

    private final Map<String, GenPropertyDescription> properties = new LinkedHashMap<>();
    public String getToStringExpression() {
        return toStringExpression;
    }

    public void setToStringExpression(String toStringExpression) {
        this.toStringExpression = toStringExpression;
    }

    public String getToLocalizableStringExpression() {
        return toLocalizableStringExpression;
    }

    public void setToLocalizableStringExpression(String toLocalizableStringExpression) {
        this.toLocalizableStringExpression = toLocalizableStringExpression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtendsId() {
        return extendsId;
    }

    public void setExtendsId(String extendsId) {
        this.extendsId = extendsId;
    }

    public Map<String, GenPropertyDescription> getProperties() {
        return properties;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }
}
