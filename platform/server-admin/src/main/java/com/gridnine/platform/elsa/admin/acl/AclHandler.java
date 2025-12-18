package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.Map;

public interface AclHandler {
    String getId();
    void fillProperties(AclObjectProxy root, Object aclObject, Object metadata, AclEngine aclEngine);

    void applyActions(AclObjectProxy obj, Object metadata, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions);

    void mergeActions(AclObjectProxy root, Object metadata);

    void applyResults(AclObjectProxy root, Object aclObject, Object metadata, AclEngine aclEngine, OperationUiContext context);
}
