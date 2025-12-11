package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.web.common.AutocompleteUtils;
import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderersRegistry {

    private Map<String, ValueRenderer<?,?,?>> renderers = new  HashMap<>();

    private Map<String, RestrictionRenderer<?,?>> restrictionRenderers = new  HashMap<>();

    @Autowired
    private AdminL10nFactory  adminL10nFactory;

    @Autowired
    private AutocompleteUtils autocompleteUtils;


    @Autowired(required = false)
    public void setValueRenderers(List<ValueRenderer<?,?,?>> renderers) {
        renderers.forEach(valueRenderer -> {
            this.renderers.put(valueRenderer.getId(), valueRenderer);
        });
    }


    @Autowired(required = false)
    public void setRestrictionRenderers(List<RestrictionRenderer<?,?>> renderers) {
        renderers.forEach(restrictionRenderer -> {
            this.restrictionRenderers.put(restrictionRenderer.getId(), restrictionRenderer);
        });
    }

    public <P,V,E extends BaseUiElement> ValueRenderer<P,V,E> getValueRenderer(String id) {
        return (ValueRenderer<P, V, E>) renderers.get(id);
    }


    public <P, E extends BaseUiElement> RestrictionRenderer<P,E> getRestrictionRenderer(String id) {
        var res = (RestrictionRenderer<P, E>) restrictionRenderers.get(id);
        if(res != null){
            return res;
        }
        if(id.startsWith(SerializablePropertyType.ENTITY_REFERENCE.name())){
            String className = id.substring(id.lastIndexOf(
            "-")+1);
             ExceptionUtils.wrapException(() ->restrictionRenderers.put(id, new EntityReferenceRestrictionRenderer<BaseIdentity>((Class) Class.forName(className), adminL10nFactory, autocompleteUtils)));
            return (RestrictionRenderer<P, E>) restrictionRenderers.get(id);
        }
        throw Xeption.forDeveloper("unsupported type" + id);
    }

}
