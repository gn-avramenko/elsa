/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

import java.util.List;

public class UiWidgetDescription extends BaseElementWithId {
    private final UiWidgetPropertiesDescription properties = new UiWidgetPropertiesDescription();

    private final UiWidgetModelDescription model = new UiWidgetModelDescription();

    private final UiWidgetConfigurationDescription configuration = new UiWidgetConfigurationDescription();

    private final UiWidgetValidationDescription validation = new UiWidgetValidationDescription();

    private String tsClassName;

    public UiWidgetDescription() {
    }

    public UiWidgetDescription(String id) {
        super(id);
    }

    public UiWidgetPropertiesDescription getProperties() {
        return properties;
    }

    public UiWidgetModelDescription getModel() {
        return model;
    }

    public UiWidgetConfigurationDescription getConfiguration() {
        return configuration;
    }

    public UiWidgetValidationDescription getValidation() {
        return validation;
    }

    public String getTsClassName() {
        return tsClassName;
    }

    public void setTsClassName(String tsClassName) {
        this.tsClassName = tsClassName;
    }
}
