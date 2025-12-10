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

package com.gridnine.platform.elsa.admin.web.acl;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclActionMetadata;
import com.gridnine.platform.elsa.admin.common.RenderersRegistry;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.admin.domain.AclRule;
import com.gridnine.platform.elsa.admin.web.common.ActionsEditor;
import com.gridnine.platform.elsa.admin.web.form.FormSelect;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AclRuleEditor extends AclRuleEditorSkeleton{

    private final RenderersRegistry renderersRegistry;

	public AclRuleEditor(String tag, OperationUiContext ctx){
		super(tag, ctx);
        var adminL10n = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(AdminL10nFactory.class);
        renderersRegistry = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(RenderersRegistry.class);
        setActionsTitle(adminL10n.Actions(), ctx);
        setConditionsTitle(adminL10n.Conditions(), ctx);
        var actionsEditor = new ActionsEditor("actions", ctx);
        addChild(ctx, actionsEditor, 0);
	}
    public void setActions(List<AclAction> actions, boolean readonly, List<AclActionMetadata<?>> actionsMetadata, OperationUiContext operationUiContext){
        var actionsEditor = (ActionsEditor) getUnmodifiableListOfChildren().get(0);
        actionsEditor.setMetadata(actionsMetadata.stream().map(it ->
                new ActionsEditor.ActionMetadata(it.getId(), it.getRendererId(), it.getRendererParameters(), it.getName().toString(LocaleUtils.getCurrentLocale()))).toList());
        actionsEditor.setData(actions.stream().map(it -> new ActionsEditor.ActionData(it.getId(), it.getValue())).toList(), readonly, operationUiContext);
    }

    @Override
    protected AclRuleEditorConfiguration createConfiguration() {
        return new AclRuleEditorConfiguration();
    }

    public boolean validate(OperationUiContext ctx) {
        var result = new AtomicBoolean(true);
        var actionsEditor = (ActionsEditor) getUnmodifiableListOfChildren().stream().filter(it -> it.getTag().equals("actions")).findFirst().get();
        if(!actionsEditor.validate(ctx)){
            result.set(false);
        }

        return result.get();
    }

    public AclRule getData() {
        var rule = new AclRule();
        var ae = (ActionsEditor) findChildByTag("actions");;
        ae.getUnmodifiableListOfChildren().forEach(ch -> {
            var fs = (FormSelect) ch.findChildByTag("action");
            if(fs.getValue() != null) {
                var rendererId = ae.getActionsMetadata().stream().filter(it -> it.id().equals(fs.getValue())).findFirst().get().rendererId();
                var renderer = renderersRegistry.getRenderer(rendererId);
                var valueComp = ch.findChildByTag("value");
                var value = ExceptionUtils.wrapException(()->renderer.getData(valueComp));
                var action = new AclAction();
                action.setId(fs.getValue());
                action.setValue(value);
                rule.getActions().add(action);
            }
        });
        return rule.getActions().isEmpty()? null:rule;
    }
}