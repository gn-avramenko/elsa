package com.gridnine.platform.elsa.admin.acl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AclObjectProxy {
    private String id;
    private AclMetadataElement aclElement;
    private final Map<String, Object> properties = new HashMap<>();
    private final Map<String, Object> totalActions = new HashMap<>();
    private final Map<String, Object> currentActions = new HashMap<>();
    private AclObjectProxy parent;

    private final List<AclObjectProxy> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AclMetadataElement getAclElement() {
        return aclElement;
    }

    public void setAclElement(AclMetadataElement aclElement) {
        this.aclElement = aclElement;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getTotalActions() {
        return totalActions;
    }

    public List<AclObjectProxy> getChildren() {
        return children;
    }

    public AclObjectProxy getParent() {
        return parent;
    }

    public void setParent(AclObjectProxy parent) {
        this.parent = parent;
    }

    public Map<String, Object> getCurrentActions() {
        return currentActions;
    }

}
