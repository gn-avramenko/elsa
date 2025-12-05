package com.gridnine.platform.elsa.admin.acl;

import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public interface AclValueRenderer<P,V,E extends BaseUiElement> {
    String getId();
    E createUiElement(P rendererParameters, V value, OperationUiContext context) throws Exception;
    V getData(E uiElement) throws Exception;
}
