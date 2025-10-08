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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.demo.ui.app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.demo.ui.account.AccountPage;
import com.gridnine.platform.elsa.demo.ui.components.test.TestAccountContainerPage;
import com.gridnine.platform.elsa.demo.ui.components.test.TestHistoryPage;
import com.gridnine.platform.elsa.demo.ui.components.test.TestMainPage;
import com.gridnine.platform.elsa.demo.ui.history.HistoryPage;
import com.gridnine.platform.elsa.demo.ui.main.MainPage;
import com.gridnine.platform.elsa.webApp.NestedRouter;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.ListableBeanFactory;

public class MainRouter extends MainRouterSkeleton{
	private String currentPath;
	private final ListableBeanFactory factory;

	public MainRouter(String tag, OperationUiContext ctx){
		super(tag, ctx);
		factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
		currentPath = getPath();
		ctx.setParameter(StandardParameters.ROUTER_PATH, currentPath);
		var viewId = getViewId(getPath());
		var elm = createElement(viewId, ctx);
		addChild(ctx, elm, 0);
	}

    @Override
    protected MainRouterConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new MainRouterConfiguration();
        JsonObject params = ctx.getParameter(OperationUiContext.PARAMS);
        result.setPath(WebPeerUtils.getString(params, "initPath"));
        result.setConfirmMessage("Are you sure you want to quit the page?");
        return result;
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
	private BaseUiElement createElement(String viewId, OperationUiContext ctx){
        ctx.setParameter(StandardParameters.BEAN_FACTORY, factory);
        if("history".equals(viewId)){
            return new HistoryPage("content", ctx);
        }
        if("account".equals(viewId)){
            return new AccountPage("content", ctx);
        }
        return new MainPage("content", ctx);
	}
	@Override
	public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception{
		if("navigate".equals(commandId)){
			var path = WebPeerUtils.getString(data.getAsJsonObject(), "path");
			var force = WebPeerUtils.getBoolean(data.getAsJsonObject(), "force", false);
			navigate(path, force, ctx);
			return;
		}
		super.processCommand(ctx, commandId, data);
	}

	private void collectNestedRouters(List<NestedRouter> nestedRouters, BaseUiElement elm){
		if(elm instanceof NestedRouter){
			nestedRouters.add((NestedRouter) elm);
		}
		elm.getUnmodifiableListOfChildren().forEach(child -> collectNestedRouters(nestedRouters, child));
	}

	private void confirm(){
		return;
	}

	public void navigate(String path, boolean force, OperationUiContext ctx){
		ctx.setParameter(StandardParameters.ROUTER_PATH, path);
		if(path.equals(currentPath)){
			return;
		}
		if(Boolean.TRUE.equals(isHasChanges()) && !force){
			confirm();
			return;
		}
		setPath(path, ctx);
		setHasChanges(false, ctx);
		var viewId = getViewId(path);
		var oldViewId = getViewId(currentPath);
		currentPath = path;
		if(viewId.equals(oldViewId)){
			var nestedRouters = new ArrayList<NestedRouter>();
			collectNestedRouters(nestedRouters, this);
			for(var nestedRouter : nestedRouters){
				nestedRouter.navigate(path, ctx);
			}
			return;
		}
		var elm = getUnmodifiableListOfChildren().getFirst();
		removeChild(ctx, elm);
		elm = createElement(viewId, ctx);
		addChild(ctx, elm, 0);
	}
}