package com.gridnine.platform.elsa.admin.acl.standard.grid;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.admin.utils.ReflectionUtils;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.BaseAdminUiContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.grid.GridContainerDescription;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.Map;

public class GridAclElementHandler implements AclHandler<GridContainerDescription> {

    @Override
    public String getId() {
        return "admin-ui-container-%s".formatted(AdminUiContainerType.GRID.name());
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, AclEngine aclEngine) {
        root.getChildren().forEach(child -> {
            var elementHandler = aclEngine.getHandler(child.getAclElement().getHandlerId());
            var childId = child.getId().substring(child.getId().lastIndexOf('.') + 1);
            ExceptionUtils.wrapException(()->elementHandler.fillProperties(root, ReflectionUtils.getChild(aclObject, childId, BaseUiElement.class), aclEngine));
        });
    }

    @Override
    public void applyActions(AclObjectProxy obj,  List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
            }

    @Override
    public void mergeActions(AclObjectProxy root) {
//noops
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, AclEngine aclEngine, OperationUiContext context) {
        root.getChildren().forEach(child -> {
            var childId = child.getId().substring(child.getId().lastIndexOf('.') + 1);
            var elementHandler = aclEngine.getHandler(child.getAclElement().getHandlerId());
            elementHandler.applyResults(root, ReflectionUtils.getChild(aclObject, childId, BaseUiElement.class) , aclEngine, context);
        });
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, GridContainerDescription element, AclEngine aclEngine) throws Exception {
        if(parent == null){
            return;
        }
        element.getRows().forEach(row -> {
            row.getColumns().forEach(column -> {
                BaseAdminUiContainerDescription container = column.getContent();
                String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
                var elementHandler = aclEngine.getHandler(handlerId);
                if(elementHandler != null){
                    ExceptionUtils.wrapException(()->elementHandler.updateAclMetadata(parent, container, aclEngine));
                }
            });
        });
    }

    @Override
    public double getPriority() {
        return 0;
    }
}
