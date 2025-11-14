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

import com.gridnine.platform.elsa.admin.domain.BaseWorkspaceItem;
import com.gridnine.platform.elsa.admin.domain.WorkspaceProjection;
import com.gridnine.platform.elsa.admin.domain.WorkspaceProjectionFields;
import com.gridnine.platform.elsa.admin.web.common.ContentWrapperConfiguration;
import com.gridnine.platform.elsa.admin.workspace.WorkspaceItemHandler;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.platform.elsa.webApp.common.ContentWrapper;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends MainFrameSkeleton{

    private ListableBeanFactory factory;
    private volatile Map<String, WorkspaceItemHandler<?>> itemHandlers;
    private WorkspaceItemHandler workspaceItemHandler;
	public MainFrame(String tag, OperationUiContext ctx) throws Exception {
		super(tag, ctx);
        var path = ctx.getParameter(OperationUiContext.PARAMS).get("initPath").getAsString();
        setEmbeddedMode(path.contains("embeddedMode=true"), ctx);
        setTitle(getMainRouter().getTitle(), ctx);
        setAppName(getTitle(), ctx);
        setBackUrl(getMainRouter().getInitBackUrl(), ctx);
        var headerToolsContainer = new ContentWrapper("header-tools", new ContentWrapperConfiguration(), ctx);
        getHeaderTools(ctx).reversed().forEach(it -> headerToolsContainer.addChild(ctx, it,0));
        addChild(ctx, headerToolsContainer, 0);
        factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        var workspaceProvider = factory.getBean(WorkspaceProvider.class);
        var workspace = workspaceProvider.getWorkspace();
        for(var group : workspace.getGroups()){
            getWorkspaceGroups().add(toWorkspaceGroup(group));
        }
	}

    protected List<BaseUiElement> getHeaderTools(OperationUiContext context) {
        return List.of(new ThemeTool(context), new LangTool(context));
    }

    @Override
    protected MainFrameConfiguration createConfiguration() {
        var mainFrameConfiguration = new MainFrameConfiguration();
        return mainFrameConfiguration;
    }

    private WorkspaceGroup toWorkspaceGroup(com.gridnine.platform.elsa.admin.domain.WorkspaceGroup group) {
        init();
        var result =  new WorkspaceGroup();
        result.setName(group.getName());
        result.setIcon(group.getIcon());
        for(var item : group.getItems()){
            var workspaceItem = new WorkspaceItem();
            workspaceItem.setName(item.getName());
            workspaceItem.setIcon(item.getIcon());
            workspaceItem.setLink(((WorkspaceItemHandler<BaseWorkspaceItem>) itemHandlers.get(item.getClass().getName())).getLink(item));
            result.getItems().add(workspaceItem);
        }
        return result;
    }

    public static MainFrame lookup(BaseUiElement elm) {
        return lookupInternal(elm);
    }

    private static MainFrame lookupInternal(BaseUiElement elm) {
        if (elm instanceof MainFrame) {
            return (MainFrame) elm;
        }
        return lookupInternal(elm.getParent());
    }

    private void init() {
        if(itemHandlers!=null){
            return;
        }
        synchronized (this) {
            if(itemHandlers==null){
                var handlers = new HashMap<String, WorkspaceItemHandler<?>>();
                factory.getBeansOfType(WorkspaceItemHandler.class).values().forEach(handler -> {
                    handlers.put(handler.getType().getName(), handler);
                });
                itemHandlers = handlers;
            }
        }
    }
}