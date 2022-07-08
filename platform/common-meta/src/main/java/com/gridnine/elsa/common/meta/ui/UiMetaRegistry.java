/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.EnumDescription;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class UiMetaRegistry {
    private final Map<String, EnumDescription> templateEnums = new LinkedHashMap<>();

    private final Map<String, UiWidgetDescription> widgets = new LinkedHashMap<>();

    private final Map<String, UiViewTemplateDescription> viewTemplates = new LinkedHashMap<>();

    private final Map<String, UiTemplateGroupDescription> groups = new LinkedHashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<UiMetaRegistryConfigurator> configurators){
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EnumDescription> getTemplateEnums() {
        return templateEnums;
    }

    public Map<String, UiWidgetDescription> getWidgets() {
        return widgets;
    }

    public Map<String, UiViewTemplateDescription> getViewTemplates() {
        return viewTemplates;
    }

    public Map<String, UiTemplateGroupDescription> getGroups() {
        return groups;
    }
}
