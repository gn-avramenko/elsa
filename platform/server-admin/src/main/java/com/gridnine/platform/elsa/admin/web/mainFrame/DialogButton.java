package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public interface DialogButton<E extends BaseUiElement> {
    String getId();
    String getName();
    void onClick(DialogCallback<E> callback, OperationUiContext context);
}
