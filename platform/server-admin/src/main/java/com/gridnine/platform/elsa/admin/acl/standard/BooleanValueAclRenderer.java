package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclValueRenderer;
import com.gridnine.platform.elsa.admin.domain.BooleanAclValue;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanField;
import com.gridnine.platform.elsa.admin.web.form.FormBooleanFieldConfiguration;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class BooleanValueAclRenderer implements AclValueRenderer<Void, BooleanAclValue, FormBooleanField> {
    public final static String RENDERER_ID = "boolean-value";

    @Override
    public String getId() {
        return RENDERER_ID;
    }

    @Override
    public FormBooleanField createUiElement(Void rendererParameters, BooleanAclValue value, OperationUiContext context) throws Exception {
        var config = new FormBooleanFieldConfiguration();
        var l10 = context.getParameter(StandardParameters.BEAN_FACTORY).getBean(AdminL10nFactory.class);
        config.setTitle(l10.Value());
        config.setValue(value != null && value.isValue());
        return new FormBooleanField("value", config, context);
    }

    @Override
    public BooleanAclValue getData(FormBooleanField uiElement) throws Exception {
        var result = new BooleanAclValue();
        result.setValue(uiElement.isValue());
        return result;
    }
}
