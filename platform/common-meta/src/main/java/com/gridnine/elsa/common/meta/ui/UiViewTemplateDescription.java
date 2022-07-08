/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

public class UiViewTemplateDescription extends BaseElementWithId {

    private String tsClassName;
    private final UiViewTemplatePropertiesDescription properties = new UiViewTemplatePropertiesDescription();
    private final UiViewTemplateContent content = new UiViewTemplateContent();

    public UiViewTemplateDescription() {
    }

    public UiViewTemplateDescription(String id) {
        super(id);
    }

    public String getTsClassName() {
        return tsClassName;
    }

    public void setTsClassName(String tsClassName) {
        this.tsClassName = tsClassName;
    }

    public UiViewTemplatePropertiesDescription getProperties() {
        return properties;
    }

    public UiViewTemplateContent getContent() {
        return content;
    }
}
