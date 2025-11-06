/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.demo.admin.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.webpeer.core.servlet.UiServletInterceptor;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd2Arguments;
import com.gridnine.webpeer.core.utils.CallableWithExceptionAnd3Arguments;
import com.gridnine.webpeer.core.utils.RunnableWithExceptionAndTwoArguments;

import java.util.List;

public class AuthInterceptor implements UiServletInterceptor<MainFrame> {
    @Override
    public MainFrame onInit(OperationUiContext context, JsonObject state, CallableWithExceptionAnd2Arguments<MainFrame, OperationUiContext, JsonObject> callback) throws Exception {
        AuthContext.setCurrentUser("admin");
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try{
           return callback.call(context, state);
        } finally {
            AuthContext.resetCurrentUser();
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public JsonElement onRequest(String commandId, JsonElement request, OperationUiContext context, CallableWithExceptionAnd3Arguments<JsonElement, String, JsonElement, OperationUiContext> callback) throws Exception {
        AuthContext.setCurrentUser("admin");
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try{
            return callback.call(commandId, request, context);
        } finally {
            AuthContext.resetCurrentUser();
            LocaleUtils.resetCurrentLocale();
        }
    }

    @Override
    public void onCommand(List<JsonObject> commands, OperationUiContext context, RunnableWithExceptionAndTwoArguments<List<JsonObject>, OperationUiContext> callback) throws Exception {
        AuthContext.setCurrentUser("admin");
        LocaleUtils.setCurrentLocale(LocaleUtils.ruLocale);
        try{
            callback.run(commands, context);
        } finally {
            AuthContext.resetCurrentUser();
            LocaleUtils.resetCurrentLocale();
        }
    }
}

