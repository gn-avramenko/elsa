package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.standard.AclElementHandler;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.EditActionMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.ViewActionMetadata;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormDateIntervalFieldDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormSelectDescription;
import org.springframework.beans.factory.annotation.Autowired;

public class FormDateIntervalElementHandler implements AclElementHandler<FormDateIntervalFieldDescription> {
    @Autowired
    private Localizer localizer;

    @Override
    public String getElementId() {
        return "admin-ui-form-%s".formatted(FormComponentType.DATE_INTERVAL_FIELD.name());
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormDateIntervalFieldDescription element, AclEngine aclEngine) throws Exception {
        var fieldMetadata = new AclMetadataElement();
        fieldMetadata.setId("%s.%s".formatted(parent.getId(), element.getId()));
        fieldMetadata.setName(LocaleUtils.createLocalizable(element.getTitle()));
        fieldMetadata.getActions().add(new AllActionsMetadata(localizer));
        fieldMetadata.getActions().add(new EditActionMetadata(localizer));
        fieldMetadata.getActions().add(new ViewActionMetadata(localizer));
        aclEngine.addNode(parent.getId(), fieldMetadata);
    }
}
