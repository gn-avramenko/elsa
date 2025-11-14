package com.gridnine.platform.elsa.admin.locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.webpeer.core.servlet.UiServletInterceptor;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd2Arguments;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd3Arguments;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndTwoArguments;

import java.util.List;

public class AdminLocaleInterceptor<T> implements UiServletInterceptor<T> {
    @Override
    public void onCommand(List<JsonObject> commands, OperationUiContext context, RunnableWithExceptionAndTwoArguments<List<JsonObject>, OperationUiContext> callback) throws Exception {
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try {
            callback.run(commands, context);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public JsonElement onRequest(String commandId, JsonElement request, OperationUiContext context, CallableWithExceptionAnd3Arguments<JsonElement, String, JsonElement, OperationUiContext> callback) throws Exception {
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try {
            return callback.call(commandId, request, context);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public T onInit(OperationUiContext context, JsonObject state, CallableWithExceptionAnd2Arguments<T, OperationUiContext, JsonObject> callback) throws Exception {
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try {
            return callback.call(context, state);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }
}
