package com.gridnine.platform.elsa.admin.web.acl;

import com.gridnine.platform.elsa.admin.domain.AclRule;
import com.gridnine.platform.elsa.admin.web.common.GroupEditor;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.List;

public class AclEntryEditor extends GroupEditor {
    public AclEntryEditor(String tag, OperationUiContext ctx) {
        super(tag, ctx);
    }

    public void setData(List<AclRule> rules, OperationUiContext context) {
        var currentChildren = new ArrayList<>(getUnmodifiableListOfChildren());
        currentChildren.forEach(it -> removeChild(context, it));
        addChild(context, ExceptionUtils.wrapException(()->createItemEditor(context)), 0);
    }

    @Override
    protected BaseUiElement createItemEditor(OperationUiContext context) throws Exception {
        return new AclRuleEditor("rule", context);
    }
}
