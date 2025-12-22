package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface AclHandler<M> {
    String getId();

    double getPriority();

    void updateAclMetadata(AclMetadataElement parent, M elementMetadata, AclEngine aclEngine) throws Exception;

    void fillProperties(AclObjectProxy root, Object aclObject, AclEngine aclEngine);

    void applyActions(AclObjectProxy obj, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions);

    void mergeActions(AclObjectProxy root);

    void applyResults(AclObjectProxy root, Object aclObject, AclEngine aclEngine, OperationUiContext context);

}
