package com.gridnine.platform.elsa.admin.acl.standard.grid;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.standard.AclElementHandler;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiContainerType;
import com.gridnine.platform.elsa.common.meta.adminUi.BaseAdminUiContainerDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.grid.GridContainerDescription;

public class GridAclElementHandler implements AclElementHandler<GridContainerDescription> {

    @Override
    public String getElementId() {
        return "admin-ui-container-%s".formatted(AdminUiContainerType.GRID.name());
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
