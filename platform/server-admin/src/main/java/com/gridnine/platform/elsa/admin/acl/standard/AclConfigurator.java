package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.domain.AclAction;

import java.util.List;
import java.util.Map;

public interface AclConfigurator {
    double getPriority();
    void updateAclMetadata(AclEngine aclEngine);
    String getId();
}
