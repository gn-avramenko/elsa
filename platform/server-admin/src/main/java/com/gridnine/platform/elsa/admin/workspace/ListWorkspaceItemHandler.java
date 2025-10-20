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

package com.gridnine.platform.elsa.admin.workspace;

import com.gridnine.platform.elsa.admin.domain.ListWorkspaceItem;
import com.gridnine.platform.elsa.admin.domain.WorkspaceItemType;
import com.gridnine.platform.elsa.admin.list.UiListHandler;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ListWorkspaceItemHandler implements WorkspaceItemHandler<ListWorkspaceItem> {

    @Autowired
    private ListableBeanFactory factory;

    private volatile Map<String, UiListHandler> uiListHandlers;


    @Override
    public Class<ListWorkspaceItem> getType() {
        return ListWorkspaceItem.class;
    }

    @Override
    public String getLink(ListWorkspaceItem item) {
        init();
        return uiListHandlers.get(item.getId()).getLink(item);
    }
    private void init() {
        if(uiListHandlers!=null){
            return;
        }
        synchronized (this) {
            if(uiListHandlers==null){
                var handlers = new HashMap<String, UiListHandler>();
                factory.getBeansOfType(UiListHandler.class).values().forEach(handler -> {
                    handlers.put(handler.getListId(), handler);
                });
                uiListHandlers = handlers;
            }
        }
    }

}
