/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistryConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiMetaRegistry {
    private final Map<String, EnumDescription> enums = new LinkedHashMap<>();
    private final Map<String,BaseViewDescription> views = new LinkedHashMap<>();
    private final Map<String, VMEntityDescription> viewModels = new LinkedHashMap<>();
    private final Map<String, VSEntityDescription> viewSettings = new LinkedHashMap<>();
    private final Map<String, VVEntityDescription> viewValidations = new LinkedHashMap<>();
    private final Map<String, CustomWidgetDescription> customWidgets = new LinkedHashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<UiMetaRegistryConfigurator> configurators){
        configurators.forEach(it -> it.updateMetaRegistry(this));
    }

    public Map<String, EnumDescription> getEnums() {
        return enums;
    }

    public Map<String, BaseViewDescription> getViews() {
        return views;
    }

    public Map<String, VMEntityDescription> getViewModels() {
        return viewModels;
    }

    public Map<String, VSEntityDescription> getViewSettings() {
        return viewSettings;
    }

    public Map<String, VVEntityDescription> getViewValidations() {
        return viewValidations;
    }

    public Map<String, CustomWidgetDescription> getCustomWidgets() {
        return customWidgets;
    }
}
