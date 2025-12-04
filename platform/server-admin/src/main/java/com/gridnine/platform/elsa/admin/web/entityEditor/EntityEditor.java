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

package com.gridnine.platform.elsa.admin.web.entityEditor;

import com.gridnine.platform.elsa.admin.web.common.ContentWrapperConfiguration;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.webApp.common.ContentWrapper;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.List;


public class EntityEditor<E extends BaseUiElement> extends EntityEditorSkeleton{

    private String title;

    private String objectId;

	public EntityEditor(OperationUiContext ctx){
		super("content", ctx);
	}

    @Override
    protected EntityEditorConfiguration createConfiguration() {
        return new EntityEditorConfiguration();
    }

    public String getTitle() {
        return title;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setTitle(String title) {}

    public void setContent(E content, OperationUiContext context) throws Exception {
        var existingContentContainer = getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals("content")).findFirst().orElse(null);
        if(existingContentContainer != null){
            removeChild(context, existingContentContainer);
        }
        addChild(context, content, 0);;
    }

    public E getContent(){
        return (E) getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals("content")).findFirst().get();
    }

    public void setTools(List<BaseUiElement> tools, OperationUiContext operationUiContext) {
        var existingToolsContainer = getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals("tools")).findFirst().orElse(null);
        if(existingToolsContainer != null){
            removeChild(operationUiContext, existingToolsContainer);
        }
        var newToolsContainer = new ContentWrapper("tools", new ContentWrapperConfiguration(), operationUiContext);
        tools.reversed().forEach(tool -> {newToolsContainer.addChild(operationUiContext, tool, 0);});
        addChild(operationUiContext, newToolsContainer, 0);
    }

    public void setTitle(String title, OperationUiContext context) {
        this.title = title;
        if(isInitialized()){
            MainFrame.lookup(this).setTitle(title, context);
        }
    }

    public void addTag(String tag, OperationUiContext context){
        var newTags = new ArrayList<>(getTags() == null? List.of() : getTags());
        if(!newTags.contains(tag)){
            newTags.add(tag);
            setTags(newTags, context);
        }
    }
    public void removeTag(String tag, OperationUiContext context){
        var newTags = new ArrayList<>(getTags() == null? List.of() : getTags());
        if(newTags.isEmpty()){
            return;
        }
        if(newTags.contains(tag)){
            newTags.remove(tag);
            setTags(newTags, context);
        }
    }
}