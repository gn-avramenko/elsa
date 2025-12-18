package com.gridnine.platform.elsa.admin.acl.standard;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.common.Conditions;
import com.gridnine.platform.elsa.admin.common.RestrictionRenderer;
import com.gridnine.platform.elsa.admin.domain.*;
import com.gridnine.platform.elsa.admin.web.common.Option;
import com.gridnine.platform.elsa.admin.web.form.*;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SimpleCriterion;
import com.gridnine.platform.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BooleanRestrictionValueRenderer implements RestrictionRenderer<Void, FormBooleanField> {

    public final static String RENDERER_ID = SerializablePropertyType.BOOLEAN.name();

    @Autowired
    private AdminL10nFactory adminL10nFactory;


    @Override
    public String getId() {
        return SerializablePropertyType.BOOLEAN.name();
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
        return result;
    }

    @Override
    public FormBooleanField createValueUiElement(Void restrictionParameters, Restriction value, String tag, OperationUiContext context) throws Exception {
        var config = new FormBooleanFieldConfiguration();
        config.setTitle(adminL10nFactory.Value());
        if (value.getValue() != null) {
            config.setValue(((BooleanRestrictionValue) value.getValue()).isValue());
        }
        config.setDeferred(true);
        return new FormBooleanField(tag, config, context);
    }

    @Override
    public Restriction getData(String propertyId, String conditionId, FormBooleanField uiElement) throws Exception {
        var restriction = new Restriction();
        restriction.setRestrictionType(RestrictionType.SIMPLE);
        restriction.setConditionId(conditionId);
        restriction.setPropertyId(propertyId);
        var value = new BooleanRestrictionValue();
        value.setValue(uiElement.isValue());
        restriction.setValue(value);
        return restriction;
    }

    @Override
    public boolean validate(String conditionId, FormBooleanField valueComp, OperationUiContext ctx) {
        return true;
    }

    @Override
    public boolean match(Object propValue, Object restrValue, String conditionId) {
        var pv = (Boolean) propValue;
        var rv = (BooleanRestrictionValue) restrValue;
        if(Conditions.EQUALS.name().equals(conditionId)){
            return pv == rv.isValue();
        }
        return pv != rv.isValue();
    }

    @Override
    public SearchCriterion getSearchCriterion(String propertyId, String conditionId, Object value) {
        return new SimpleCriterion(propertyId, Conditions.EQUALS.name().equals(conditionId)? SimpleCriterion.Operation.EQ: SimpleCriterion.Operation.NE, value);
    }

}
