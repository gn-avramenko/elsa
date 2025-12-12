package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.core.model.common.L10nMessage;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;

import java.util.ArrayList;
import java.util.List;

public class AclMetadataElement {
    private Localizable name;
    private String id;
    private String handlerId;
    private String parentId;
    private final List<AclActionMetadata<?>> actions = new ArrayList<>();
    private final List<AclPropertyMetadata<?>> properties = new ArrayList<>();

    private final List<AclMetadataElement> children = new ArrayList<>();

    public Localizable getName() {
        return name;
    }
    public void setName(L10nMessage name, Localizer localizer) {
        this.name = LocaleUtils.createLocalizable(name, localizer);
    }
    public void setName(Localizable name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AclMetadataElement> getChildren() {
        return children;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AclActionMetadata<?>> getActions() {
        return actions;
    }

    public List<AclPropertyMetadata<?>> getProperties() {
        return properties;
    }
}
