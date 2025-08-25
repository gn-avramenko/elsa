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
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.ArrayList;

public class TestAccountRouter extends BaseWebAppUiElement implements TestNestedRouter{

    private String currentPath;

    public TestAccountRouter(String tag, OperationUiContext ctx) {
        super("account.AccountRouter", tag, ctx);
        var config = this.createConfiguration(ctx);
        this.currentPath = config.getPath();
        var viewId = getViewId(currentPath);
        var elm = createElement(viewId, ctx);
        addChild(ctx, elm, 0);
        decorateWithListeners();
    }

    private String getViewId(String path) {
        if(path.contains("/account/organizations")){
            return "organizations";
        }
        if(path.contains("/account/doctors")){
            return "doctors";
        }
        if(path.contains("/account/managers")){
            return "managers";
        }
        if(path.contains("/account/clients")){
            return "clients";
        }
        if(path.contains("/account/account")){
            return "account";
        }
        if(path.contains("/account/security")){
            return "security";
        }
        throw new RuntimeException("Invalid path");
    }

    private TestAccountRouterConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestAccountRouterConfiguration();
        JsonObject params = ctx.getParameter(OperationUiContext.PARAMS);
        var path = ctx.getParameter(TestWebAppRouter.ROUTER_PATH);
        result.setPath(path == null? WebPeerUtils.getString(params, "initPath"): path);
        return result;
    }

    private void decorateWithListeners() {
    }

    private BaseWebAppUiElement createElement(String viewId, OperationUiContext ctx) {
        return switch (viewId){
            case "organizations" -> new TestOrganizationsSection("content", ctx);
            case "doctors" -> new TestDoctorsSection("content", ctx);
            case "managers" -> new TestManagersSection("content", ctx);
            case "clients" -> new TestClientsSection("content", ctx);
            case "account" -> new TestAccountSection("content", ctx);
            case "security" -> new TestSecuritySection("content", ctx);
            default -> throw new RuntimeException("Invalid viewId");
        };
    }


    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        getUnmodifiableListOfChildren().getFirst().restoreFromState(WebPeerUtils.getDynamic(state, "content"), ctx);
    }

    @Override
    public void navigate(String path, OperationUiContext ctx) {
        if(path.equals(this.currentPath)){
            return;
        }
        String newViewId = getViewId(path);
        String oldViewId = getViewId(this.currentPath);
        this.currentPath = path;
        if(oldViewId.equals(newViewId)){
            var nestedRouters = new ArrayList<TestNestedRouter>();
            collectNestedRouters(nestedRouters, this);
            nestedRouters.forEach(nestedRouter -> nestedRouter.navigate(newViewId, ctx));
            return;
        }
        var elm = getUnmodifiableListOfChildren().getFirst();
        removeChild(ctx, elm);
        elm = createElement(newViewId, ctx);
        addChild(ctx, elm, 0);

    }
    private void collectNestedRouters(ArrayList<TestNestedRouter> nestedRouters, BaseUiElement testWebAppRouter) {
        if(testWebAppRouter instanceof TestNestedRouter){
            nestedRouters.add((TestNestedRouter) testWebAppRouter);
        }
        testWebAppRouter.getUnmodifiableListOfChildren().forEach(child -> collectNestedRouters(nestedRouters, child));
    }

}
