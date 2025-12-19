package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.admin.domain.BooleanValueWrapper;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class RootNodeAclHandler implements AclHandler<Void> {

    public static final String ROOT_NODE_ID="root";

    @Autowired
    private Localizer localizer;

    @Override
    public double getPriority() {
        return -1;
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, Void elementMetadata, AclEngine aclEngine) throws Exception {
        var rootElement = new AclMetadataElement();
        rootElement.setId(RootNodeAclHandler.ROOT_NODE_ID);
        rootElement.setHandlerId(getId());
        rootElement.setName(AdminL10nFactory.All_ObjectsMessage(), localizer);
        rootElement.getActions().add(new AllActionsMetadata(localizer));
        aclEngine.addNode(null, rootElement);
    }

    @Override
    public String getId() {
        return ROOT_NODE_ID;
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, Void metadata, AclEngine aclEngine) {
        root.getChildren().forEach(child -> {
            aclEngine.getHandler(child.getAclElement().getHandlerId()).fillProperties(child, aclObject, null, aclEngine);
        });
    }

    @Override
    public void applyActions(AclObjectProxy obj, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
        actions.forEach(action -> {
            var value = ((BooleanValueWrapper)action.getValue()).isValue();
            obj.getCurrentActions().put(AllActionsMetadata.ACTION_ID, value);
        });
        if(actions.isEmpty()){
            obj.getCurrentActions().put(AllActionsMetadata.ACTION_ID, false);
        }

    }

    @Override
    public void mergeActions(AclObjectProxy root) {
        if(!root.getTotalActions().isEmpty() && (Boolean) root.getTotalActions().get(AllActionsMetadata.ACTION_ID)){
            return;
        }
        root.getTotalActions().putAll(root.getCurrentActions());
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, Void metadata, AclEngine aclEngine, OperationUiContext  context) {
        //noops
    }
}
