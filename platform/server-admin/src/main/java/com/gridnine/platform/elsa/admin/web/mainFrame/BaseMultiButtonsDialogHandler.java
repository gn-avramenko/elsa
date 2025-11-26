package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.function.Consumer;

public abstract class BaseMultiButtonsDialogHandler<E extends BaseUiElement, P> implements DialogHandler<E,P>{

    @Override
    public E getEditor(String tag, OperationUiContext context, P parameters) {
        return ExceptionUtils.wrapException(()-> createEditor(tag, parameters, context));
    }

    protected DialogButton<E> button(String id, String title, RunnableWithExceptionAnd2Arguments<DialogCallback<E>, OperationUiContext> clickListener, Consumer<MainFrameDialogButtonConfiguration> customizer) {
        return new DialogButton<E>() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getName() {
                return title;
            }

            @Override
            public void onClick(DialogCallback<E> callback, OperationUiContext context) {
                ExceptionUtils.wrapException(()->clickListener.run(callback, context));
            }

            @Override
            public void customize(MainFrameDialogButtonConfiguration bc) {
                if(customizer != null) {
                    customizer.accept(bc);
                }
            }
        };
    }

    protected abstract E createEditor(String tag, P params, OperationUiContext context) throws Exception;


}
