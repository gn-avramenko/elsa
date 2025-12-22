package com.gridnine.platform.elsa.admin.acl.standard.form;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.acl.standard.*;
import com.gridnine.platform.elsa.admin.common.RestrictionsValueRenderer;
import com.gridnine.platform.elsa.admin.domain.*;
import com.gridnine.platform.elsa.admin.utils.LocaleUtils;
import com.gridnine.platform.elsa.admin.web.form.FormDateIntervalField;
import com.gridnine.platform.elsa.admin.web.form.FormRemoteMultiSelect;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormBooleanFieldDescription;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.common.meta.adminUi.form.FormRemoteMultiSelectDescription;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class FormRemoteMultiSelectAclElementHandler implements AclHandler<FormRemoteMultiSelectDescription> {
    @Autowired
    private Localizer localizer;

    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public String getId() {
        return "admin-ui-form-%s".formatted(FormComponentType.REMOTE_MULTI_SELECT.name());
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, FormRemoteMultiSelectDescription element, AclEngine aclEngine) throws Exception {
        if(parent == null){
            return;
        }
        var fieldMetadata = new AclMetadataElement();
        fieldMetadata.setId("%s.%s".formatted(parent.getId(), element.getId()));
        fieldMetadata.setName(LocaleUtils.createLocalizable(element.getTitle()));
        fieldMetadata.setHandlerId(getId());
        fieldMetadata.getActions().add(new AllActionsMetadata(localizer));
        fieldMetadata.getActions().add(new EditActionMetadata(localizer));
        fieldMetadata.getActions().add(new ViewActionMetadata(localizer));
        aclEngine.addNode(parent.getId(), fieldMetadata);
    }
    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, AclEngine aclEngine) {
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
            if(action.getId().equals(AllActionsMetadata.ACTION_ID)){
                var value = ((BooleanValueWrapper)action.getValue()).isValue();
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
            } else if (action.getId().equals(ViewActionMetadata.ACTION_ID)){
                var value = ((BooleanValueWrapper)action.getValue()).isValue();
                obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
            } else if (action.getId().equals(EditActionMetadata.ACTION_ID)){
                var value = ((BooleanValueWrapper)action.getValue()).isValue();
                obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
            }else if (action.getId().equals(ListRestrictionsMetadata.ACTION_ID)){
                var value = (RestrictionsValueWrapper)action.getValue();
                obj.getCurrentActions().put(ListRestrictionsMetadata.ACTION_ID, value.getRestrictions());
            }
        });
    }

    @Override
    public void mergeActions(AclObjectProxy obj) {
        var view = Boolean.TRUE.equals(obj.getTotalActions().get(ViewActionMetadata.ACTION_ID));
        if(!view){
            obj.getTotalActions().put(ViewActionMetadata.ACTION_ID, obj.getCurrentActions().get(ViewActionMetadata.ACTION_ID));
        }
        var edit = Boolean.TRUE.equals(obj.getTotalActions().get(EditActionMetadata.ACTION_ID));
        if(!edit){
            obj.getTotalActions().put(EditActionMetadata.ACTION_ID, obj.getCurrentActions().get(EditActionMetadata.ACTION_ID));
        }
        var firstTime = obj.getTotalActions().isEmpty();
        var currentRestrictions = (List<Restriction>) obj.getCurrentActions().get(ListRestrictionsMetadata.ACTION_ID);
        var totalRestrictions = (List<Restriction>) obj.getTotalActions().get(ListRestrictionsMetadata.ACTION_ID);
        if(firstTime){
            if(currentRestrictions != null && !currentRestrictions.isEmpty()){
                obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
            }
            return;
        }
        if(totalRestrictions != null && !totalRestrictions.isEmpty()){
            if(currentRestrictions == null || currentRestrictions.isEmpty()){
                obj.getTotalActions().remove(ListRestrictionsMetadata.ACTION_ID);
                return;
            }
            var compoundRestriction = new Restriction();
            compoundRestriction.setRestrictionType(RestrictionType.OR);
            var restr1 =  new Restriction();
            if(totalRestrictions.size() > 1) {
                restr1.setRestrictionType(RestrictionType.AND);
                restr1.getNestedRestrictions().addAll(totalRestrictions);
            } else {
                restr1 = totalRestrictions.get(0);
            }
            var restr2 =  new Restriction();
            if(currentRestrictions.size() > 1) {
                restr2.setRestrictionType(RestrictionType.AND);
                restr2.getNestedRestrictions().addAll(currentRestrictions);
            } else {
                restr2 = currentRestrictions.get(0);
            }
            compoundRestriction.getNestedRestrictions().add(restr1);
            compoundRestriction.getNestedRestrictions().add(restr2);
            obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
            return;
        }
        if(currentRestrictions == null || currentRestrictions.isEmpty()){
            obj.getTotalActions().remove(ListRestrictionsMetadata.ACTION_ID);
            return;
        }
        obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, AclEngine aclEngine, OperationUiContext context) {
        if(aclObject instanceof FormRemoteMultiSelect field){
            field.setReadonlyByAcl(!Boolean.TRUE.equals(root.getTotalActions().get(EditActionMetadata.ACTION_ID)), context);
            List<Restriction> restrictions = (List<Restriction>) root.getTotalActions().get(ListRestrictionsMetadata.ACTION_ID);
            if(restrictions != null && !restrictions.isEmpty()){
                var act = root.getAclElement().getActions().stream().filter(it -> it.getId().equals(ListRestrictionsMetadata.ACTION_ID)).findFirst().get();
                var crit = aclEngine.toSearchCriterion(restrictions, ((RestrictionsValueRenderer.RestrictionsValueParameters)act.getRendererParameters()).properties());
                field.setAdditionalCriterion(crit);
            }
        }
    }
}
