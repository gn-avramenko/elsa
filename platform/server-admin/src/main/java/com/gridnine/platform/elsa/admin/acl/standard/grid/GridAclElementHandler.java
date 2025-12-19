package com.gridnine.platform.elsa.admin.acl.standard.grid;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.acl.standard.AclElementHandler;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.BaseAdminUiContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.grid.GridContainerDescription;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.Map;

public class GridAclElementHandler implements AclElementHandler<GridContainerDescription> {

    @Override
    public String getId() {
        return "admin-ui-container-%s".formatted(AdminUiContainerType.GRID.name());
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject,Object metadata, AclEngine aclEngine) {
        var description = (GridContainerDescription) metadata;
        description.getRows().forEach( r->
                r.getColumns().forEach(c->{
                    BaseAdminUiContainerDescription container = c.getContent();
                    String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
                    var elementHandler = aclEngine.getElementHandler(handlerId);
                    if(elementHandler != null){
                        ExceptionUtils.wrapException(()->elementHandler.fillProperties(root, aclObject, metadata, aclEngine));
                    }
                })
        );

    }

    @Override
    public void applyActions(AclObjectProxy obj, Object metadata, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
        var description = (GridContainerDescription) metadata;
        description.getRows().forEach( r->
                r.getColumns().forEach(c->{
                    BaseAdminUiContainerDescription container = c.getContent();
                    String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
                    var elementHandler = aclEngine.getElementHandler(handlerId);
                    if(elementHandler != null){
                        ExceptionUtils.wrapException(()->elementHandler.applyActions(obj, container, actions, aclEngine, parentActions));
                    }
                })
        );

    }

    @Override
    public void mergeActions(AclObjectProxy root, Object metadata) {
//noops
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, Object metadata, AclEngine aclEngine, OperationUiContext context) {
        ((GridContainerDescription) metadata).getRows().forEach(row -> {
            row.getColumns().forEach(column -> {
                BaseAdminUiContainerDescription container = column.getContent();
                String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
                aclEngine.getElementHandler(handlerId).applyResults(root, aclObject, container, aclEngine, context);
            });
        });
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, GridContainerDescription element, AclEngine aclEngine) throws Exception {
        element.getRows().forEach(row -> {
            row.getColumns().forEach(column -> {
                BaseAdminUiContainerDescription container = column.getContent();
                String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
                var elementHandler = aclEngine.getElementHandler(handlerId);
                if(elementHandler != null){
                    ExceptionUtils.wrapException(()->elementHandler.updateAclMetadata(parent, container, aclEngine));
                }
            });
        });
    }

}
