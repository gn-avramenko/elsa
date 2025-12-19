package com.gridnine.platform.elsa.admin.common;

public class BasicAclObject {
    private boolean accessAllowed;

    public void setAccessAllowed(boolean accessAllowed) {
        this.accessAllowed = accessAllowed;
    }

    public boolean isAccessAllowed() {
        return accessAllowed;
    }
}
