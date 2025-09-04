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
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.ArrayList;

public class TestWebAppRouter extends BaseWebAppUiElement {

    public final static TypedParameter<String> ROUTER_PATH = new TypedParameter<>("router-path");

    private String currentPath;

    private final ListableBeanFactory factory;

    public TestWebAppRouter(String tag, OperationUiContext ctx) {
        super("app.WebAppRouter", tag, ctx);
        factory = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY);
        var config = this.createConfiguration(ctx);
        setPath(config.getPath(), ctx);
        setHasChanges(false, ctx);
        setConfirmMessage(config.getConfirmMessage(), ctx);
        currentPath = config.getPath();
        ctx.setParameter(TestWebAppRouter.ROUTER_PATH, currentPath);
        var viewId = getViewId(config.getPath());
        var elm = createElement(viewId, ctx);
        addChild(ctx, elm, 0);
        decorateWithListeners();
    }

    public boolean isHasChanges(){
        return getProperty("hasChanges", Boolean.class);
    }

    public void setHasChanges(boolean value, OperationUiContext ctx){
        setProperty("hasChanges", value, ctx);
    }

    public void setPath(String path, OperationUiContext ctx) {
        setProperty("path", path, ctx);
    }

    public String getPath() {
        return getProperty("path", String.class);
    }

    public void setConfirmMessage(String confirmMessage, OperationUiContext ctx) {
        setProperty("confirmMessage", confirmMessage, ctx);
    }


    private TestWebAppRouterConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestWebAppRouterConfiguration();
        JsonObject params = ctx.getParameter(OperationUiContext.PARAMS);
        result.setPath(WebPeerUtils.getString(params, "initPath"));
        result.setConfirmMessage("There is unsaved changes. Are you sure you want to continue?");
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
        ctx.setParameter(SimpleSiteWebAppServlet.BEAN_FACTORY, factory);
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
            var force = WebPeerUtils.getBoolean(data.getAsJsonObject(), "force", false);
            navigate(path, force, ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }

    public void navigate(String path, boolean force, OperationUiContext ctx) {
        ctx.setParameter(ROUTER_PATH, path);
        if(path.equals(currentPath)){
            return;
        }
        if(isHasChanges() && !force){
            var cmdData = new JsonObject();
            cmdData.addProperty("path", path);
            cmdData.addProperty("force", true);
            TestWebApp.lookup(this).confirm("There unsaved changes. Continue?",  getId(), "navigate", cmdData, ctx);
            return;
        }
        setProperty("path", path, ctx);
        setHasChanges(false, ctx);
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
        var elm = (BaseWebAppUiElement)getUnmodifiableListOfChildren().getFirst();
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

}
