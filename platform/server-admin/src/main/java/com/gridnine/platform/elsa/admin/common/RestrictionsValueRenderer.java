package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.domain.BooleanValueWrapper;
import com.gridnine.platform.elsa.admin.domain.RestrictionsValueWrapper;
import com.gridnine.platform.elsa.admin.web.common.RestrictionsEditor;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanField;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanFieldConfiguration;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RestrictionsValueRenderer implements ValueRenderer<RestrictionsValueRenderer.RestrictionsValueParameters, RestrictionsValueWrapper, RestrictionsEditor> {
    public final static String RENDERER_ID = "restrictions";

    @Autowired
    private AdminL10nFactory adminL10nFactory;

    @Override
    public String getId() {
        return RENDERER_ID;
    }

    @Override
    public RestrictionsEditor createUiElement(RestrictionsValueParameters rendererParameters, RestrictionsValueWrapper value, boolean readonly, String tag, OperationUiContext context) throws Exception {
        var result = new RestrictionsEditor(tag, context);
        result.setPropertiesMetadata(rendererParameters.properties);
        result.setData(value == null? List.of(): value.getRestrictions(), readonly, context);
        return result;
    }

    @Override
    public RestrictionsValueWrapper getData(RestrictionsEditor uiElement) throws Exception {
        return uiElement.getData();
    }

    @Override
    public boolean validate(RestrictionsEditor valueComp, OperationUiContext ctx) {
        return valueComp.validate(ctx);
    }


    public record RestrictionsValueParameters(List<RestrictionsEditor.RestrictionPropertyMetadata> properties){}
}
