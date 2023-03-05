/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class L10nMessage extends BaseIntrospectableObject{
    private static final String keyPropertyName = "key";
    private static final String bundlePropertyName = "bundle";
    private static final String parametersPropertyName = "parameters";

    public L10nMessage(){}

    public L10nMessage(String bundle, String key, Object... parameters){
        this.bundle = bundle;
        this.key = key;
        Collections.addAll(this.parameters, parameters);
    }

    private String key;

    private String bundle;

    private final List<Object> parameters = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public Object getValue(String propertyName) {
        return switch (propertyName){
            case keyPropertyName -> key;
            case bundlePropertyName -> bundle;
            default -> super.getValue(propertyName);
        };
    }

    @Override
    public void setValue(String propertyName, Object value) {
        switch (propertyName){
            case keyPropertyName -> key = (String) value;
            case bundlePropertyName -> bundle = (String) value;
            default -> super.setValue(propertyName, value);
        }
    }

    @Override
    public Collection<?> getCollection(String collectionName) {
        if(parametersPropertyName.equals(collectionName)){
            return getParameters();
        }
        return super.getCollection(collectionName);
    }


}
