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
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.GlobalUiContext;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.ArrayList;

public class TestWebAppRouter extends BaseWebAppUiElement {

    public final static TypedParameter<String> ROUTER_PATH = new TypedParameter<>("router-path");

    private String currentPath;

    public TestWebAppRouter(String tag, OperationUiContext ctx) {
        super("app.WebAppRouter", tag, ctx);
        var config = this.createConfiguration(ctx);
        setPath(config.getPath(), ctx);
        var viewId = getViewId(config.getPath());
        var elm = createElement(viewId, ctx);
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

    private String getViewId(String path){
        if(path != null && path.contains("/history")){
            return "history";
        }
        if(path != null && path.contains("/account")){
            return "account";
        }
        return "main";
    }
    private BaseWebAppUiElement createElement(String viewId, OperationUiContext ctx) {
        if("history".equals(viewId)){
            return new TestHistoryPage("content", ctx);
        }
        if("account".equals(viewId)){
            return new TestAccountContainerPage("content", ctx);
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
        ctx.setParameter(ROUTER_PATH, path);
        if(path.equals(currentPath)){
            return;
        }
        var viewId = getViewId(path);
        var oldViewId = getViewId(currentPath);
        currentPath = path;
        if(viewId.equals(oldViewId)){
            var nestedRouters = new ArrayList<TestNestedRouter>();
            collectNestedRouters(nestedRouters, this);
            nestedRouters.forEach(nestedRouter -> {
                nestedRouter.navigate(path, ctx);
            });
            return;
        }
        var elm = getUnmodifiableListOfChildren().getFirst();
        removeChild(ctx, elm);
        elm = createElement(viewId, ctx);
        addChild(ctx, elm, 0);
    }

    private void collectNestedRouters(ArrayList<TestNestedRouter> nestedRouters, BaseUiElement testWebAppRouter) {
        if(testWebAppRouter instanceof TestNestedRouter){
            nestedRouters.add((TestNestedRouter) testWebAppRouter);
        }
        testWebAppRouter.getUnmodifiableListOfChildren().forEach(child -> collectNestedRouters(nestedRouters, child));
    }

    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        getUnmodifiableListOfChildren().getFirst().restoreFromState(WebPeerUtils.getDynamic(state, "content"), ctx);
    }
}
