package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.common.core.model.common.Localizable;

public class AclPropertyMetadata<P> {
    private String id;
    private Localizable name;
    private String restrictionRendererId;
    private P restrictionRendererParameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Localizable getName() {
        return name;
    }

    public void setName(Localizable name) {
        this.name = name;
    }

    public String getRestrictionRendererId() {
        return restrictionRendererId;
    }

    public void setRestrictionRendererId(String restrictionRendererId) {
        this.restrictionRendererId = restrictionRendererId;
    }

    public P getRestrictionRendererParameters() {
        return restrictionRendererParameters;
    }

    public void setRestrictionRendererParameters(P restrictionRendererParameters) {
        this.restrictionRendererParameters = restrictionRendererParameters;
    }
}
