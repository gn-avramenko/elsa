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

package com.gridnine.platform.elsa.demo.ui.components.test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.servlet.WebAppModule;
import com.gridnine.webpeer.core.ui.GlobalUiContext;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

public class TestWebAppRouter extends BaseWebAppUiElement {

    public TestWebAppRouter(String tag, OperationUiContext ctx) {
        super("app.WebAppRouter", tag, ctx);
        var config = this.createConfiguration(ctx);
        setPath(config.getPath(), ctx);
        var elm = createElement(ctx);
        addChild(ctx, elm, 0);
        decorateWithListeners();
    }

    public void setPath(String path, OperationUiContext ctx) {
        setProperty("path", path, ctx);
    }

    public String getPath() {
        return getProperty("path", String.class);
    }
    private TestWebAppRouterConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestWebAppRouterConfiguration();
        JsonObject params = ctx.getParameter(OperationUiContext.PARAMS);
        result.setPath(WebPeerUtils.getString(params, "initPath"));
        return result;
    }

    private void decorateWithListeners() {
    }

    private BaseWebAppUiElement createElement(OperationUiContext ctx) {
        String path = getPath();
        if(path != null && path.contains("/history")){
            return new TestHistoryPage("content", ctx);
        }
        return new TestMainPage("content", ctx);
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if("navigate".equals(commandId)) {
            var path = WebPeerUtils.getString(data.getAsJsonObject(), "path");
            navigate(path, ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }

    public void navigate(String path, OperationUiContext ctx) {
        setProperty("path", path, ctx);
        var elm = getUnmodifiableListOfChildren().getFirst();
        removeChild(ctx, elm);
        elm = createElement(ctx);
        addChild(ctx, elm, 0);
    }

    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        getUnmodifiableListOfChildren().getFirst().restoreFromState(WebPeerUtils.getDynamic(state, "content"), ctx);
    }
}
