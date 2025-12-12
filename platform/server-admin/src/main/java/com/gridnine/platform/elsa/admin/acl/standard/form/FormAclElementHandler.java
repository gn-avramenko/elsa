package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.standard.AclElementHandler;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormCustomElementDescription;

public class FormAclElementHandler implements AclElementHandler<FormContainerDescription> {
    @Override
    public String getElementId() {
        return "admin-ui-container-%s".formatted(AdminUiContainerType.FORM.name());
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormContainerDescription element, AclEngine aclEngine) throws Exception {
        element.getComponents().values().forEach(elm ->{
            var handlerId = "admin-ui-form-%s".formatted(elm.getType() == FormComponentType.CUSTOM? ((FormCustomElementDescription) elm).getClassName(): elm.getType().name());
            var handler = aclEngine.getElementHandler(handlerId);
            if(handler != null){
                ExceptionUtils.wrapException(()->handler.updateAclMetadata(parent, elm, aclEngine));
            }

        });
    }
}
