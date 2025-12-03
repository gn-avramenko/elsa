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

package com.gridnine.platform.elsa.admin.web.mainFrame;

import com.google.gson.JsonElement;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.webApp.NestedRouter;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.ListableBeanFactory;

public class MainRouter extends MainRouterSkeleton{
	private RouterPathHandler lastHandler;
	private final ListableBeanFactory factory;
    private volatile List<RouterPathHandler> routerPathHandlers;
	public MainRouter(String tag, OperationUiContext ctx){
		super(tag, ctx);
		factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        var path = ctx.getParameter(OperationUiContext.PARAMS).get("initPath").getAsString();
        setPath(path, ctx);
		ctx.setParameter(StandardParameters.ROUTER_PATH, getPath());
		var handler =getRouterPathHandler(getPath());
        lastHandler = handler;
		var elm = ExceptionUtils.wrapException(()-> handler.createElement(getPath(), ctx));
        addChild(ctx, elm, 0);
	}

    public String getInitBackUrl(){
        return lastHandler.getDefaultBackUrl(getPath());
    }
    private RouterPathHandler getRouterPathHandler(String path){
        init();
        var p = path;
        if(p.contains("?")){
            p = p.substring(0, p.indexOf("?")-1);
        }
        var cp = p.startsWith("/") ? p.substring(1): p;
        var handler = routerPathHandlers.stream().filter(it -> it.canHandle(cp)).findFirst().orElse(null);
        if(handler == null){
            throw Xeption.forDeveloper("Unable to find handler for path " + path);
        }
        return handler;
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

    @Override
    protected MainRouterConfiguration createConfiguration() {
        var mainRouterConfiguration = new MainRouterConfiguration();
        mainRouterConfiguration.setPath("/home");
        mainRouterConfiguration.setConfirmMessage("Are you sure you want to go back?");
        mainRouterConfiguration.setHasChanges(false);
        return mainRouterConfiguration;
    }

    private void collectNestedRouters(List<NestedRouter> nestedRouters, BaseUiElement elm){
		if(elm instanceof NestedRouter){
			nestedRouters.add((NestedRouter) elm);
		}
		elm.getUnmodifiableListOfChildren().forEach(child -> collectNestedRouters(nestedRouters, child));
	}

	private void confirm(){
		//noops
	}

	public void navigate(String path, boolean force, OperationUiContext ctx){
        var oldPath = getPath();
        ctx.setParameter(StandardParameters.ROUTER_PATH, path);
        ctx.setParameter(StandardParameters.BEAN_FACTORY, factory);
        var handler = getRouterPathHandler(path);
        MainFrame.lookup(this).setBackUrl(handler.getDefaultBackUrl(path), ctx);
        if(lastHandler != null && lastHandler.equals(handler) && Objects.equals(oldPath, path)){
			return;
		}
        if(Boolean.TRUE.equals(isHasChanges()) && !force){
			confirm();
			return;
		}
		setPath(path, ctx);
		setHasChanges(false, ctx);
        lastHandler = handler;
		var elm = getUnmodifiableListOfChildren().getFirst();
        removeChild(ctx, elm);
        elm = ExceptionUtils.wrapException(()->handler.createElement(path, ctx));
        addChild(ctx, elm, 0);
        MainFrame.lookup(this).setTitle(ExceptionUtils.wrapException(()->lastHandler.getTitle(getPath(), ctx)), ctx);
    }

    private void init() {
        if(routerPathHandlers!=null){
            return;
        }
        synchronized (this) {
            if(routerPathHandlers==null){
                var handlers = new ArrayList<RouterPathHandler>();
                factory.getBeansOfType(RouterPathHandler.class).values().forEach(handler -> {
                    handlers.add(handler);
                });
                routerPathHandlers = handlers;
            }
        }
    }

    public String getTitle(OperationUiContext ctx) throws Exception {
        return lastHandler.getTitle(getPath(), ctx);
    }
}