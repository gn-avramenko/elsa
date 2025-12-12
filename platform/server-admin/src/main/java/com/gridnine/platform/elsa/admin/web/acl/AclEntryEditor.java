package com.gridnine.platform.elsa.admin.web.acl;

import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.domain.AclEntry;
import com.gridnine.platform.elsa.admin.domain.AclRule;
import com.gridnine.platform.elsa.admin.web.common.GroupEditor;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AclEntryEditor extends GroupEditor {

    private volatile AclMetadataElement metadataElement;

    public AclEntryEditor(String tag, OperationUiContext ctx) {
        super(tag, ctx);
    }

    public void setData(AclMetadataElement elementMetadata, List<AclRule> rules, OperationUiContext context) {
        this.metadataElement = elementMetadata;
        var currentChildren = new ArrayList<>(getUnmodifiableListOfChildren());
        currentChildren.forEach(it -> removeChild(context, it));
        if (rules.isEmpty()) {
            addChild(context, ExceptionUtils.wrapException(() -> createItemEditor(context)), 0);
            return;
        }
        for (int n = 0; n < rules.size(); n++) {
            var editor = (AclRuleEditor) ExceptionUtils.wrapException(() -> createItemEditor(context));
            var rule = rules.get(n);
            editor.setActions(rule.getActions(), elementMetadata.getActions(), context);
            editor.setConditions(rule.getConditions(), elementMetadata.getProperties(), context);
            addChild(context, editor, n);
        }
    }

    @Override
    protected BaseUiElement createItemEditor(OperationUiContext context) throws Exception {
        var editor = new AclRuleEditor("rule", context);
        editor.setActions(List.of(), metadataElement.getActions(), context);
        editor.setConditions(List.of(), metadataElement.getProperties(), context);
        return editor;
    }

    public boolean validate(OperationUiContext ctx) {
        AtomicBoolean hasErrors = new AtomicBoolean(true);
        getUnmodifiableListOfChildren().forEach(it -> {
            if (!((AclRuleEditor) it).validate(ctx)) {
                hasErrors.set(false);
            }
        });
        return hasErrors.get();
    }

    public AclEntry getData() {
        var result = new AclEntry();
        result.setId(metadataElement.getId());
        getUnmodifiableListOfChildren().forEach(it -> {
            AclRule rule = ((AclRuleEditor) it).getData();
            if(rule != null) {
                result.getRules().add(rule);
            }
        });
        return result.getRules().isEmpty()? null: result;
    }
}
