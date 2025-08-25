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
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

public class TestAccountRouter extends BaseWebAppUiElement {

    public TestAccountRouter(String tag, OperationUiContext ctx) {
        super("account.AccountRouter", tag, ctx);
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

    private TestAccountRouterConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestAccountRouterConfiguration();
        JsonObject params = ctx.getParameter(OperationUiContext.PARAMS);
        var path = ctx.getParameter(TestWebAppRouter.ROUTER_PATH);
        result.setPath(path == null? WebPeerUtils.getString(params, "initPath"): path);
        return result;
    }

    private void decorateWithListeners() {
    }

    private BaseWebAppUiElement createElement(OperationUiContext ctx) {
        String path = getPath();
        if(path != null && path.contains("/account/organizations")){
            return new TestOrganizationsSection("content", ctx);
        }
        if(path != null && path.contains("/account/doctors")){
            return new TestDoctorsSection("content", ctx);
        }
        if(path != null && path.contains("/account/managers")){
            return new TestManagersSection("content", ctx);
        }
        if(path != null && path.contains("/account/clients")){
            return new TestClientsSection("content", ctx);
        }
        if(path != null && path.contains("/account/account")){
            return new TestAccountSection("content", ctx);
        }
        if(path != null && path.contains("/account/security")){
            return new TestSecuritySection("content", ctx);
        }
        return new TestOrganizationsSection("content", ctx);
    }


    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        getUnmodifiableListOfChildren().getFirst().restoreFromState(WebPeerUtils.getDynamic(state, "content"), ctx);
    }
}
