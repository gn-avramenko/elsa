package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.web.dialog.LabelHeader;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithException;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseOkCancelDialogHandler<E extends BaseUiElement, P> implements DialogHandler<P>{

    @Autowired
    private AdminL10nFactory  adminL10nFactory;

    @Override
    public MainFrame.DialogPack getDialogPack(OperationUiContext context, P parameters) throws Exception {
        var header = new MainFrameDialogHeader("dialog-header", context);
        {
            var title = new LabelHeader("title", context);
            title.setTitle(getTitle(parameters), context);
            header.addChild(context, title, 0);
        }
        var content = createEditor("dialog-content", parameters, context);
        var footer = new MainFrameDialogFooter("dialog-footer", context);
        {
            var config = new MainFrameDialogButtonConfiguration();
            config.setTitle(adminL10nFactory.Cancel());
            config.setClickListener((ctx) ->{
                MainFrame.lookup(content).closeDialog(ctx);
            });
            var cancelButton = new MainFrameDialogButton("cancel", config, context);
            footer.addChild(context, cancelButton, 0);
        }
        {
            var config = new MainFrameDialogButtonConfiguration();
            config.setTitle(adminL10nFactory.Ok());
            config.setClickListener((ctx) ->{
                onOk(ctx, content, ()->{
                    MainFrame.lookup(content).closeDialog(ctx);
                });
            });
            var okButton = new MainFrameDialogButton("ok", config, context);
            footer.addChild(context, okButton, 0);
        }
        return new MainFrame.DialogPack(header, content, footer);
    }

    protected abstract String getTitle(P parameters);

    protected abstract E createEditor(String tag, P params, OperationUiContext context) throws Exception;

    protected abstract void onOk(OperationUiContext context, E editor, RunnableWithException closeCallback) throws Exception;
}
