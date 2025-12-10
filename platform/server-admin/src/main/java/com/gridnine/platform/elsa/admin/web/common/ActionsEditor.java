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

package com.gridnine.platform.elsa.admin.web.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.common.RenderersRegistry;
import com.gridnine.platform.elsa.admin.common.ValueRenderer;
import com.gridnine.platform.elsa.admin.web.form.FormSelect;
import com.gridnine.platform.elsa.admin.web.form.FormSelectConfiguration;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionsEditor extends ActionsEditorSkeleton{

    private List<ActionMetadata> actionsMetadata = new ArrayList<>();

    private final AdminL10nFactory adminL10nFactory;
    private final RenderersRegistry renderersRegistry;
    private final ListableBeanFactory beanFactory;

    public void setMetadata(List<ActionMetadata> actions) {
        this.actionsMetadata = actions;
    }

    public ActionsEditor(String tag, OperationUiContext ctx){
		super(tag, ctx);
        beanFactory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        adminL10nFactory = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(AdminL10nFactory.class);
        renderersRegistry = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(RenderersRegistry.class);
	}

    public void setData(List<ActionData> actions, boolean readonly, OperationUiContext ctx){
        setReadonly(readonly, ctx);
        var currentChildren = new ArrayList<>(getUnmodifiableListOfChildren());
        currentChildren.forEach(it -> removeChild(ctx, it));
        if(actions.isEmpty()){
            addChild(ctx, ExceptionUtils.wrapException(()->createAction(null, ctx)), 0);
            return;
        }
        actions.forEach(action -> {
            addChild(ctx, ExceptionUtils.wrapException(()->createAction(action, ctx)), 0);
        });
    }

    private BaseUiElement createAction(ActionData data, OperationUiContext ctx) throws Exception {
        var result = new ContentWrapper("action", new ContentWrapperConfiguration(), ctx);
        var actionConfig  = new FormSelectConfiguration();
        actionConfig.setDeferred(false);
        actionConfig.setTitle(adminL10nFactory.Action());
        actionConfig.setReadonly(isReadonly());
        actionConfig.setValue(data == null? null: data.id);
        actionConfig.getOptions().addAll(actionsMetadata.stream().map(it ->{
            var item = new Option();
            item.setId(it.id);
            item.setDisplayName(it.title);
            return item;
        }).toList());
        actionConfig.setValueChangeListener((oldVal, newValue, context) -> {
            context.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
            var mtd = actionsMetadata.stream().filter(it -> it.id.equals(newValue)).findFirst().get();
            createValueEditor(result, mtd.rendererId, mtd.renderersParams, null, context);
        });
        var action = new FormSelect("action", actionConfig, ctx);
        result.addChild(ctx, action, 0);
        if(data != null){
            var mtd = actionsMetadata.stream().filter(it -> it.id.equals(data.id)).findFirst().get();
            createValueEditor(result, mtd.rendererId, mtd.renderersParams, data.value, ctx);
        }
        return result;
    }

    private void createValueEditor(ContentWrapper contentWrapper, String rendererId, Object parameters, Object value, OperationUiContext ctx) throws Exception {
        var existingComp = contentWrapper.getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals("value")).findFirst().orElse(null);
        if(existingComp != null){
            contentWrapper.removeChild(ctx, existingComp);
        }
        var renderer = renderersRegistry.getRenderer(rendererId);
        var elm = renderer.createUiElement(parameters, value, isReadonly(), "value", ctx);
        contentWrapper.addChild(ctx, elm, 0);
    }

    @Override
    protected ActionsEditorConfiguration createConfiguration() {
        var result = new ActionsEditorConfiguration();
        result.setAddListener((act, ctx)->{
            var editor = createAction(null, ctx);
            addChild(ctx, editor, act.getIdx()+1);
        });
        result.setDeleteListener((act, ctx)->{
            var child = getUnmodifiableListOfChildren().get(act.getIdx());
            removeChild(ctx, child);
            if(getUnmodifiableListOfChildren().isEmpty()){
                var editor = createAction(null, ctx);
                addChild(ctx, editor, 0);
            }
        });
        result.setMoveDownListener((act, ctx)->{
            var child = getUnmodifiableListOfChildren().get(act.getIdx());
            moveChild(ctx, child.getId(), act.getIdx()+1);
        });
        result.setMoveUpListener((act, ctx)->{
            var child = getUnmodifiableListOfChildren().get(act.getIdx());
            moveChild(ctx, child.getId(), act.getIdx()-1);
        });
        return result;
    }

    public boolean validate(OperationUiContext ctx) {
        var result = new AtomicBoolean(true);
        getUnmodifiableListOfChildren().forEach(child -> {
            var actionControl = (FormSelect) child.findChildByTag("action");
            if(actionControl != null && actionControl.getValue() != null){
                var rendererId = actionsMetadata.stream().filter(it -> it.id.equals(actionControl.getValue())).findFirst().get().rendererId;
                var renderer = renderersRegistry.getRenderer(rendererId);
                var valueComp = child.findChildByTag("value");
                if(!renderer.validate(valueComp, ctx)){
                    result.set(false);
                }
            }
        });
        return result.get();
    }

    public List<ActionMetadata> getActionsMetadata() {
        return actionsMetadata;
    }

    public record ActionMetadata(String id, String rendererId, Object renderersParams, String title){}

    public record ActionData(String id, Object value){}
}