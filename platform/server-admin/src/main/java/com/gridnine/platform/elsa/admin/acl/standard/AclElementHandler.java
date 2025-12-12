package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;

public interface AclElementHandler<E> {
    String getElementId();
    void updateAclMetadata(AclMetadataElement parent, E element, AclEngine aclEngine) throws Exception;
}
