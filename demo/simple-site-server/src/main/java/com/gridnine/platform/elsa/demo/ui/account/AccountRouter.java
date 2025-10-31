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

package com.gridnine.platform.elsa.demo.ui.account;

import com.gridnine.platform.elsa.demo.ui.account.account.AccountSection;
import com.gridnine.platform.elsa.demo.ui.account.client.ClientsSection;
import com.gridnine.platform.elsa.demo.ui.account.doctor.DoctorsSection;
import com.gridnine.platform.elsa.demo.ui.account.manager.ManagersSection;
import com.gridnine.platform.elsa.demo.ui.account.organization.OrganizationEditor;
import com.gridnine.platform.elsa.demo.ui.account.organization.OrganizationsSection;
import com.gridnine.platform.elsa.demo.ui.account.security.SecuritySection;
import com.gridnine.platform.elsa.webApp.NestedRouter;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountRouter extends AccountRouterSkeleton{
	private String currentPath;
	private final ListableBeanFactory factory;

	public AccountRouter(String tag, OperationUiContext ctx){
		super(tag, ctx);
		factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
		var config = createConfiguration();
        var params = ctx.getParameter(OperationUiContext.PARAMS);
        var path = ctx.getParameter(StandardParameters.ROUTER_PATH);
        config.setPath(path == null? WebPeerUtils.getString(params, "initPath"): path);
		currentPath = config.getPath();
		ctx.setParameter(StandardParameters.ROUTER_PATH, currentPath);
		var viewId = getViewId(config.getPath());
		var elm = createElement(viewId, ctx);
		addChild(ctx, elm, 0);
	}
	private String getViewId(String path){
        if(path.contains("/account/organizations/")){
            return "organization";
        }
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
	private BaseUiElement createElement(String viewId, OperationUiContext ctx){
        ctx.setParameter(StandardParameters.BEAN_FACTORY, factory);
        return switch (viewId){
            case "organizations" -> new OrganizationsSection("content", ctx);
            case "doctors" -> new DoctorsSection("content", ctx);
            case "managers" -> new ManagersSection("content", ctx);
            case "clients" -> new ClientsSection("content", ctx);
            case "account" -> new AccountSection("content", ctx);
            case "security" -> new SecuritySection("content", ctx);
            case "organization" -> new OrganizationEditor("content", ctx);
            default -> throw new RuntimeException("Invalid viewId");
        };
	}

	private void collectNestedRouters(List<NestedRouter> nestedRouters, BaseUiElement elm){
		if(elm instanceof NestedRouter){
			nestedRouters.add((NestedRouter) elm);
		}
		elm.getUnmodifiableListOfChildren().forEach(child -> collectNestedRouters(nestedRouters, child));
	}

	public void navigate(String path, OperationUiContext ctx){
		ctx.setParameter(StandardParameters.ROUTER_PATH, path);
		if(path.equals(currentPath)){
			return;
		}
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

	@Override
	protected AccountRouterConfiguration createConfiguration(){
		var result = new AccountRouterConfiguration();
		return result;
	}
}