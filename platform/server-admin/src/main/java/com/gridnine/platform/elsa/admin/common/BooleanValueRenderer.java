package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.domain.BooleanValueWrapper;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanField;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanFieldConfiguration;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

public class BooleanValueRenderer implements ValueRenderer<Void, BooleanValueWrapper, FormBooleanField> {
    public final static String RENDERER_ID = "boolean-value";

    @Autowired
    private AdminL10nFactory adminL10nFactory;

    @Override
    public String getId() {
        return RENDERER_ID;
    }

    @Override
    public FormBooleanField createUiElement(Void rendererParameters, BooleanValueWrapper value, boolean readonly, String tag, OperationUiContext context) throws Exception {
        var config = new FormBooleanFieldConfiguration();
        config.setTitle(adminL10nFactory.Value());
        config.setValue(value != null && value.isValue());
        config.setReadonly(readonly);
        return new FormBooleanField(tag, config, context);
    }

    @Override
    public BooleanValueWrapper getData(FormBooleanField uiElement) throws Exception {
        var result = new BooleanValueWrapper();
        result.setValue(uiElement.isValue());
        return result;
    }

    @Override
    public boolean validate(FormBooleanField valueComp, OperationUiContext ctx) {
        return true;
    }
}
