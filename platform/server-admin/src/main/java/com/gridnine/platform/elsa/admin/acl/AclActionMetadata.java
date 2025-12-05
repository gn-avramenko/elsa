package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.common.core.model.common.Localizable;

public class AclActionMetadata<P> {
    private String id;
    private Localizable name;
    private String rendererId;
    private P rendererParameters;

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

    public String getRendererId() {
        return rendererId;
    }

    public void setRendererId(String rendererId) {
        this.rendererId = rendererId;
    }

    public P getRendererParameters() {
        return rendererParameters;
    }

    public void setRendererParameters(P rendererParameters) {
        this.rendererParameters = rendererParameters;
    }
}
