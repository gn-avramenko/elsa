package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.EditActionMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.ViewActionMetadata;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.admin.domain.BooleanValueWrapper;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.admin.web.form.FormSelect;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormSelectDescription;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class FormSelectAclElementHandler implements AclHandler<FormSelectDescription> {
    @Autowired
    private Localizer localizer;

    @Override
    public String getId() {
        return "admin-ui-form-%s".formatted(FormComponentType.SELECT.name());
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormSelectDescription element, AclEngine aclEngine) throws Exception {
        if(parent == null){
            return;
        }
        var fieldMetadata = new AclMetadataElement();
        fieldMetadata.setId("%s.%s".formatted(parent.getId(), element.getId()));
        fieldMetadata.setHandlerId(getId());
        fieldMetadata.setName(LocaleUtils.createLocalizable(element.getTitle()));
        fieldMetadata.getActions().add(new AllActionsMetadata(localizer));
        fieldMetadata.getActions().add(new EditActionMetadata(localizer));
        fieldMetadata.getActions().add(new ViewActionMetadata(localizer));
        aclEngine.addNode(parent.getId(), fieldMetadata);
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, FormSelectDescription metadata,AclEngine aclEngine) {
        //noops
    }

    @Override
    public void applyActions(AclObjectProxy obj, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
        parentActions.forEach((k,v) ->{
            if(AllActionsMetadata.ACTION_ID.equals(k)){
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID,v);
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, v);
            } else if (EditActionMetadata.ACTION_ID.equals(k)) {
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, v);
            } else if (ViewActionMetadata.ACTION_ID.equals(k)) {
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, v);
            }
        });
        actions.forEach(action -> {
            var value = ((BooleanValueWrapper)action.getValue()).isValue();
            if(action.getId().equals(AllActionsMetadata.ACTION_ID)){
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
            } else if (action.getId().equals(ViewActionMetadata.ACTION_ID)){
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
            } else if (action.getId().equals(EditActionMetadata.ACTION_ID)){
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
            }
        });
    }

    @Override
    public void mergeActions(AclObjectProxy obj, FormSelectDescription metadata) {
        var view = Boolean.TRUE.equals(obj.getTotalActions().get(ViewActionMetadata.ACTION_ID));
        if(!view){
            obj.getTotalActions().put(ViewActionMetadata.ACTION_ID, obj.getCurrentActions().get(ViewActionMetadata.ACTION_ID));
        }
        var edit = Boolean.TRUE.equals(obj.getTotalActions().get(EditActionMetadata.ACTION_ID));
        if(!edit){
            obj.getTotalActions().put(EditActionMetadata.ACTION_ID, obj.getCurrentActions().get(EditActionMetadata.ACTION_ID));
        }
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, FormSelectDescription metadata, AclEngine aclEngine, OperationUiContext context) {
        if(aclObject instanceof FormSelect field){
            field.setReadonly(!Boolean.TRUE.equals(root.getTotalActions().get(EditActionMetadata.ACTION_ID)), context);
        }
    }

    @Override
    public double getPriority() {
        return 0;
    }
}
