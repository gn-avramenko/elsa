package com.gridnine.platform.elsa.admin.acl;

public interface AclHandler {
    double getPriority();
    void updateAclMetadata(AclEngine aclEngine);
    default String getId(){
        return getClass().getName();
    }
}
