package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormCustomElementDescription;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.Map;

public class FormAclElementHandler implements AclHandler<FormContainerDescription> {
    @Override
    public String getId() {
        return "admin-ui-container-%s".formatted(AdminUiContainerType.FORM.name());
    }

    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject,FormContainerDescription metadata, AclEngine aclEngine) {
        var description = (FormContainerDescription) metadata;
        root.getChildren().forEach(child -> {
           aclEngine.getHandler(child.getAclElement().getHandlerId()).fillProperties(child, aclObject, description.getComponents().get(child.getId().substring(child.getId().lastIndexOf(".")+1)) , aclEngine);
        });
        root.getChildren().forEach(child -> {
            child.getProperties().putAll(root.getProperties());
        });
    }

    @Override
    public void applyActions(AclObjectProxy obj, FormContainerDescription metadata, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
//noops
    }

    @Override
    public void mergeActions(AclObjectProxy root, FormContainerDescription metadata) {
//noops
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, FormContainerDescription metadata, AclEngine aclEngine,  OperationUiContext context) {
        var description = (FormContainerDescription) metadata;
        root.getChildren().forEach(child -> {
            var childId = child.getId().substring(child.getId().lastIndexOf(".")+1);
            aclEngine.getHandler(child.getAclElement().getHandlerId()).applyResults(child, getElement(aclObject, childId, BaseUiElement.class),
                    description.getComponents().get( childId), aclEngine, context);
        });
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormContainerDescription element, AclEngine aclEngine) throws Exception {
        if(parent == null){
            return;
        }
        element.getComponents().values().forEach(elm ->{
            var handlerId = "admin-ui-form-%s".formatted(elm.getType() == FormComponentType.CUSTOM? ((FormCustomElementDescription) elm).getClassName(): elm.getType().name());
            var handler = aclEngine.getHandler(handlerId);
            if(handler != null){
                ExceptionUtils.wrapException(()->handler.updateAclMetadata(parent, elm, aclEngine));
            }
        });
        parent.getChildren().forEach(child -> {
            child.getProperties().addAll(parent.getProperties());
        });
    }
}
