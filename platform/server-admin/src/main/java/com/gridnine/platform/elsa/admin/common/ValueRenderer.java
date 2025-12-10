package com.gridnine.platform.elsa.admin.common;

import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public interface ValueRenderer<P,V,E extends BaseUiElement> {
    String getId();
    E createUiElement(P rendererParameters, V value, boolean readonly, String tag, OperationUiContext context) throws Exception;
    V getData(E uiElement) throws Exception;
    boolean validate(E valueComp, OperationUiContext ctx);
}
