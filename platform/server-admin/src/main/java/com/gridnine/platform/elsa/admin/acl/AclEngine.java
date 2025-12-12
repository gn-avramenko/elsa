package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.standard.AclElementHandler;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.admin.common.ValueRenderer;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AclEngine {

    public static final String ROOT_NODE_ID="root";

    private AclMetadataElement rootElement;

    private final Map<String, AclMetadataElement> inverseMap = new HashMap<>();

    @Autowired(required = false)
    private List<AclHandler> handlers;

    @Autowired(required = false)
    private List<AclElementHandler> elementHandlers;


    private final Map<String, AclElementHandler> elementsHandlersMap = new HashMap<>();

    @Autowired
    private Localizer localizer;

    public AclMetadataElement getNode(String nodeId) {
        return inverseMap.get(nodeId);
    }

    public void addNode(String parentId, AclMetadataElement element) {
        element.setParentId(parentId);
        var elm = inverseMap.get(parentId);
        elm.getChildren().add(element);
        if(element.getId() != null){
            inverseMap.put(element.getId(), element);
        }
    }

    public<E> AclElementHandler<E> getElementHandler(String elementId) {
        return (AclElementHandler<E>) elementsHandlersMap.get(elementId);
    }
    @PostConstruct
    public void init() {
        rootElement = new AclMetadataElement();
        rootElement.setId(ROOT_NODE_ID);
        rootElement.setName(AdminL10nFactory.All_ObjectsMessage(), localizer);
        rootElement.getActions().add(new AllActionsMetadata(localizer));
        inverseMap.put(ROOT_NODE_ID, rootElement);
        if(elementHandlers != null) {
            elementHandlers.forEach(it -> elementsHandlersMap.put(it.getElementId(), it));
        }
        if (handlers != null) {
            for (AclHandler handler : handlers.stream().sorted(Comparator.comparing(AclHandler::getPriority)).toList()) {
                handler.updateAclMetadata(this);
            }
        }
    }
}
