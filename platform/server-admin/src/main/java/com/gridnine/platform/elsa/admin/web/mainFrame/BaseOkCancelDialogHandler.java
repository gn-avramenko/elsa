package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public abstract class BaseOkCancelDialogHandler<E extends BaseUiElement, P> implements DialogHandler<E,P>{

    @Autowired
    private AdminL10nFactory  adminL10nFactory;

    @Override
    public E getEditor(String tag, OperationUiContext context, P parameters) {
        return ExceptionUtils.wrapException(()-> createEditor(tag, parameters, context));
    }

    @Override
    public List<DialogButton<E>> getButtons(OperationUiContext context) {
        var cancelButton = new DialogButton<E>(){

            @Override
            public String getId() {
                return "cancel";
            }

            @Override
            public String getName() {
                return adminL10nFactory.Cancel();
            }

            @Override
            public void onClick(DialogCallback<E> callback, OperationUiContext context) {
                callback.close();
            }
        };
        var okButton = new DialogButton<E>(){

            @Override
            public String getId() {
                return "ok";
            }

            @Override
            public String getName() {
                return adminL10nFactory.Ok();
            }

            @Override
            public void onClick(DialogCallback<E> callback, OperationUiContext context) {
                ExceptionUtils.wrapException(()-> onOk(context, callback));
            }
        };
        return Arrays.asList(okButton, cancelButton);
    }

    protected abstract E createEditor(String tag, P params, OperationUiContext context) throws Exception;

    protected abstract void onOk(OperationUiContext context, DialogCallback<E> callback) throws Exception;
}
