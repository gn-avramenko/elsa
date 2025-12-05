package com.gridnine.platform.elsa.admin.acl;

public interface AclHandler {
    double getPriority();
    void updateAclMetadata(AclEngine aclEngine);
}
