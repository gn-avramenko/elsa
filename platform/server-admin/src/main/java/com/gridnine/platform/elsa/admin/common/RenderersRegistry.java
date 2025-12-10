package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderersRegistry {

    private Map<String, ValueRenderer<?,?,?>> renderers = new  HashMap<>();

    @Autowired(required = false)
    public void setConfigurators(List<ValueRenderer<?,?,?>> renderers) {
        renderers.forEach(valueRenderer -> {
            this.renderers.put(valueRenderer.getId(), valueRenderer);
        });
    }


    public <P,V,E extends BaseUiElement> ValueRenderer<P,V,E> getRenderer(String id) {
        return (ValueRenderer<P, V, E>) renderers.get(id);
    }


}
