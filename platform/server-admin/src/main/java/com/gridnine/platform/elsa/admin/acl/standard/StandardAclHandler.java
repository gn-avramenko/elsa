package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;

public class StandardAclHandler implements AclHandler {
    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public void updateAclMetadata(AclEngine aclEngine) {
        aclEngine.register(new BooleanValueAclRenderer());
    }
}
