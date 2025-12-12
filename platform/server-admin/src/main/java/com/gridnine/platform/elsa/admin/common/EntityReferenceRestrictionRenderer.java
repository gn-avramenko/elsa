package com.gridnine.platform.elsa.admin.common;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.domain.EntityReferenceRestrictionValue;
import com.gridnine.platform.elsa.admin.domain.EntityReferenceWrapper;
import com.gridnine.platform.elsa.admin.domain.Restriction;
import com.gridnine.platform.elsa.admin.domain.RestrictionType;
import com.gridnine.platform.elsa.admin.web.common.AutocompleteUtils;
import com.gridnine.platform.elsa.admin.web.common.Option;
import com.gridnine.platform.elsa.admin.web.form.FormRemoteMultiSelect;
import com.gridnine.platform.elsa.admin.web.form.FormRemoteMultiSelectConfiguration;
import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.List;

public class EntityReferenceRestrictionRenderer<E extends BaseIdentity> implements RestrictionRenderer<Void, FormRemoteMultiSelect> {

    private final Class<E> entityClass;
    private final AdminL10nFactory adminL10nFactory;
    private final AutocompleteUtils autocompleteUtils;

    public EntityReferenceRestrictionRenderer(Class<E> entityClass, AdminL10nFactory adminL10nFactory, AutocompleteUtils autocompleteUtils) {
        this.entityClass = entityClass;
        this.adminL10nFactory = adminL10nFactory;
        this.autocompleteUtils = autocompleteUtils;
    }


    @Override
    public String getId() {
        return "%s-%s".formatted(SerializablePropertyType.ENTITY_REFERENCE.name(), entityClass.getSimpleName());
    }

    @Override
    public List<Option> getConditions() {
        var result = new ArrayList<Option>();
        {
            var eq = new Option();
            eq.setId(Conditions.EQUALS.name());
            eq.setDisplayName(adminL10nFactory.Equals());
            result.add(eq);
        }
        {
            var neq = new Option();
            neq.setId(Conditions.NOT_EQUALS.name());
            neq.setDisplayName(adminL10nFactory.Not_Equals());
            result.add(neq);
        }
        return result;
    }

    @Override
    public FormRemoteMultiSelect createValueUiElement(Void restrictionParameters, Restriction value, String tag, OperationUiContext context) throws Exception {
        var config = new FormRemoteMultiSelectConfiguration();
        config.setTitle(adminL10nFactory.Value());
        if (value != null) {
            var refs = (EntityReferenceRestrictionValue) value.getValue();
            if (refs != null) {
                config.getValue().addAll(refs.getValues().stream().map(it -> {
                    var opt = new Option();
                    opt.setId(it.getId());
                    opt.setDisplayName(it.getDisplayName());
                    return opt;
                }).toList());
            }
        }
        config.setDeferred(true);
        config.setAutocompleteServiceHandler(autocompleteUtils.createMultiSelectAutocomplete(entityClass));
        var result = new FormRemoteMultiSelect(tag, config, context);
        return result;
    }

    @Override
    public Restriction getData(String propertyId, String conditionId, FormRemoteMultiSelect uiElement) throws Exception {
        var restriction = new Restriction();
        restriction.setRestrictionType(RestrictionType.SIMPLE);
        restriction.setConditionId(conditionId);
        restriction.setPropertyId(propertyId);
        var value = new EntityReferenceRestrictionValue();
        value.getValues().addAll(uiElement.getValue().stream().map(it -> {
            var opt = new EntityReferenceWrapper();
            opt.setId(it.getId());
            opt.setDisplayName(it.getDisplayName());
            return opt;
        }).toList());
        restriction.setValue(value);
        return restriction;
    }


    @Override
    public boolean validate(String conditionId, FormRemoteMultiSelect valueComp, OperationUiContext ctx) {
        if (valueComp.getValue().isEmpty()) {
            valueComp.setValidation(adminL10nFactory.Value_Empty(), ctx);
            return false;
        }
        return true;
    }
}
