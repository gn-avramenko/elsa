package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.webpeer.core.ui.OperationUiContext;

public interface DialogHandler<P> {
    MainFrame.DialogPack getDialogPack(OperationUiContext context, P parameters) throws Exception;
}
