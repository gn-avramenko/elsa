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
import com.gridnine.platform.elsa.admin.domain.WorkspaceItemType;
import com.gridnine.platform.elsa.admin.domain.WorkspaceProjection;
import com.gridnine.platform.elsa.admin.domain.WorkspaceProjectionFields;
import com.gridnine.platform.elsa.admin.workspace.WorkspaceItemHandler;
import com.gridnine.platform.elsa.core.auth.AuthContext;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.HashMap;
import java.util.Map;

public class MainFrame extends MainFrameSkeleton{

    private Storage storage;

    private ListableBeanFactory factory;
    private volatile Map<Class<?>, WorkspaceItemHandler<?>> itemHandlers;

	public MainFrame(String tag, OperationUiContext ctx){
		super(tag, ctx);
	}

    @Override
    protected MainFrameConfiguration createConfiguration(OperationUiContext ctx) {
        var mainFrameConfiguration = new MainFrameConfiguration();
        mainFrameConfiguration.setFlexDirection(FlexDirection.COLUMN);
        factory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        storage = factory.getBean(Storage.class);
        var workspace = storage.findUniqueDocument(WorkspaceProjection.class, WorkspaceProjectionFields.userLogin, AuthContext.getCurrentUser(), false);
        for(var group : workspace.getGroups()){
            mainFrameConfiguration.getWorkspaceGroups().add(toWorkspaceGroup(group));
        }
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
            workspaceItem.setLink(((WorkspaceItemHandler<BaseWorkspaceItem>) itemHandlers.get(item.getClass())).getLink(item));
            result.getItems().add(workspaceItem);
        }
        return result;
    }

    private void init() {
        if(itemHandlers!=null){
            return;
        }
        synchronized (this) {
            if(itemHandlers==null){
                itemHandlers = new HashMap<>();
                factory.getBeansOfType(WorkspaceItemHandler.class).values().forEach(handler -> {
                    itemHandlers.put(handler.getType(), handler);
                });
            }
        }
    }
}