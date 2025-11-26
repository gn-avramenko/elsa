package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;

public interface DialogHandler<E extends BaseUiElement, P> {
    public E getEditor(String tag, OperationUiContext context, P parameters);
    public List<DialogButton<E>> getButtons(OperationUiContext context);
    default public BaseUiElement getCustomHeader(E editor, OperationUiContext context){
        return null;
    }
}
