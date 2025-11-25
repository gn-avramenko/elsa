package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public interface DialogCallback<E extends BaseUiElement> {
    void close();
    E getEditor();
}
