package com.gridnine.platform.elsa.admin.locale;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.admin.utils.AdminParameters;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.servlet.UiServletInterceptor;
import com.gridnine.webpeer.core.ui.GlobalUiContext;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd2Arguments;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd3Arguments;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndTwoArguments;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Locale;

public class AdminLocaleInterceptor<T> implements UiServletInterceptor<T> {

    @Autowired
    private ListableBeanFactory beanFactory;

    @Override
    public void onCommand(List<JsonObject> commands, OperationUiContext context, RunnableWithExceptionAndTwoArguments<List<JsonObject>, OperationUiContext> callback) throws Exception {
        LocaleUtils.setCurrentLocale(GlobalUiContext.getParameter(context.getParameter(OperationUiContext.PATH), context.getParameter(OperationUiContext.CLIENT_ID), AdminParameters.LOCALE));
        context.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
        try {
            callback.run(commands, context);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public JsonElement onRequest(String commandId, JsonElement request, OperationUiContext context, CallableWithExceptionAnd3Arguments<JsonElement, String, JsonElement, OperationUiContext> callback) throws Exception {
        LocaleUtils.setCurrentLocale(GlobalUiContext.getParameter(context.getParameter(OperationUiContext.PATH), context.getParameter(OperationUiContext.CLIENT_ID), AdminParameters.LOCALE));
        context.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
        try {
            return callback.call(commandId, request, context);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public T onInit(OperationUiContext context, JsonObject state, CallableWithExceptionAnd2Arguments<T, OperationUiContext, JsonObject> callback) throws Exception {
        var ls = context.getParameter(OperationUiContext.LOCAL_STORAGE_DATA);
        var path = context.getParameter(OperationUiContext.PARAMS).get("initPath").getAsString();
        if(path.contains("embeddedMode=true")){
            var locale = path.contains("lang=ru")? LocaleUtils.ruLocale: Locale.ENGLISH;
            GlobalUiContext.setParameter(context.getParameter(OperationUiContext.PATH), context.getParameter(OperationUiContext.CLIENT_ID), AdminParameters.LOCALE, locale);
            LocaleUtils.setCurrentLocale(locale);
        } else {
            var ruLang = ls.has("lang") && ls.get("lang").getAsString().equals("ru");
            var locale = ruLang? LocaleUtils.ruLocale: Locale.ENGLISH;
            GlobalUiContext.setParameter(context.getParameter(OperationUiContext.PATH), context.getParameter(OperationUiContext.CLIENT_ID), AdminParameters.LOCALE, locale);
            LocaleUtils.setCurrentLocale(locale);
        }
        context.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
        try {
            return callback.call(context, state);
        } finally {
            LocaleUtils.resetCurrentLocale();
        }
    }
}
