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
import com.gridnine.platform.elsa.admin.domain.Restriction;
import com.gridnine.platform.elsa.admin.domain.RestrictionType;
import com.gridnine.platform.elsa.admin.domain.RestrictionsValueWrapper;
import com.gridnine.platform.elsa.admin.web.form.FormSelect;
import com.gridnine.platform.elsa.admin.web.form.FormSelectConfiguration;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RestrictionsEditor extends RestrictionsEditorSkeleton {
    private List<RestrictionPropertyMetadata> propertiesMetadata = new ArrayList<>();

    private final AdminL10nFactory adminL10nFactory;
    private final RenderersRegistry renderersRegistry;

    public void setPropertiesMetadata(List<RestrictionPropertyMetadata> propertiesMetadata) {
        this.propertiesMetadata = propertiesMetadata;
    }

    public RestrictionsEditor(String tag, OperationUiContext ctx) {
        super(tag, ctx);
        adminL10nFactory = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(AdminL10nFactory.class);
        renderersRegistry = ctx.getParameter(StandardParameters.BEAN_FACTORY).getBean(RenderersRegistry.class);
        setL10nAnd(adminL10nFactory.l10nAnd(), ctx);
        setL10nNot(adminL10nFactory.l10nNot(), ctx);
        setL10nOr(adminL10nFactory.l10nOr(), ctx);
        setL10nSimple(adminL10nFactory.l10nSimple(), ctx);
        setAddListener((act, context) ->{
            var  re = findRestrictionEditor(act.getElementId());
            BaseUiElement parent = re.getParent();
            var idx = parent.getUnmodifiableListOfChildren().indexOf(re);
            var children = new ArrayList<BaseUiElement>();
            var restr = new Restriction();
            restr.setRestrictionType(RestrictionType.valueOf(act.getRestrictionType()));
            if(restr.getRestrictionType() != RestrictionType.SIMPLE){
                var nestedRestriction = new Restriction();
                nestedRestriction.setRestrictionType(RestrictionType.SIMPLE);
                restr.getNestedRestrictions().add(nestedRestriction);
            }
            collectChildren(children, List.of(restr), context);
            if(re.getRestrictionType() == com.gridnine.platform.elsa.admin.web.common.RestrictionType.SIMPLE){
                var fs = (FormSelect) re.findChildByTag("property");
                if(fs.getValue() == null){
                    parent.removeChild(context, re);
                    parent.addChild(context, children.get(0), 0);
                    return;
                }
            }
            parent.addChild(context, children.get(0), idx+1);
        });
        setDeleteListener((act, context) -> {
            var  re = findRestrictionEditor(act.getElementId());
            BaseUiElement parent = re.getParent();
            parent.removeChild(context, re);
            if(parent.getUnmodifiableListOfChildren().isEmpty()){
                var rest = new Restriction();
                rest.setRestrictionType(RestrictionType.SIMPLE);
                var children = new ArrayList<BaseUiElement>();
                collectChildren(children, List.of(rest), context);
                parent.addChild(context, children.get(0), 0);
            }
        });
        setMoveDownListener((act, context) -> {
            var  re = findRestrictionEditor(act.getElementId());
            BaseUiElement parent = re.getParent();
            var idx = parent.getUnmodifiableListOfChildren().indexOf(re);
            if(idx < parent.getUnmodifiableListOfChildren().size()-1){
                parent.moveChild(context, Long.parseLong(act.getElementId()), idx+1);
            }
        });
        setMoveUpListener((act, context) -> {
            var  re = findRestrictionEditor(act.getElementId());
            BaseUiElement parent = re.getParent();
            var idx = parent.getUnmodifiableListOfChildren().indexOf(re);
            if(idx > 0){
                parent.moveChild(context, Long.parseLong(act.getElementId()), idx-1);
            }
        });
    }

    private RestrictionEditor findRestrictionEditor(String id) {
        var lid = Long.parseLong(id);
        return findRestrictionEditorInternal(lid, this.getUnmodifiableListOfChildren());
    }

    private RestrictionEditor findRestrictionEditorInternal(long lid, List<BaseUiElement> unmodifiableListOfChildren) {
        for(BaseUiElement child : unmodifiableListOfChildren){
            if(child.getId() == lid){
                return (RestrictionEditor) child;
            }
            if(!child.getUnmodifiableListOfChildren().isEmpty()){
                var rest = findRestrictionEditorInternal(lid, child.getUnmodifiableListOfChildren());
                if(rest != null){
                    return rest;
                }
            }
        }
        return null;
    }


    public void setData(List<Restriction> restrictions,  OperationUiContext ctx) {
        var currentChildren = new ArrayList<>(getUnmodifiableListOfChildren());
        currentChildren.forEach(it -> removeChild(ctx, it));
        var children = new ArrayList<BaseUiElement>();
        var restr2 = new ArrayList<Restriction>(restrictions);
        if (restr2.isEmpty()) {
            var item = new Restriction();
            item.setRestrictionType(RestrictionType.SIMPLE);
            restr2.add(item);
        }
        collectChildren(children, restr2, ctx);
        for (int n = 0; n < children.size(); n++) {
            addChild(ctx, children.get(n), n);
        }
    }

    private void collectChildren(ArrayList<BaseUiElement> children, List<Restriction> restrictions, OperationUiContext ctx) {
        restrictions.forEach(it -> {
            var child = new RestrictionEditor("child", ctx);
            if (it.getRestrictionType() == RestrictionType.SIMPLE) {
                child.setRestrictionType(com.gridnine.platform.elsa.admin.web.common.RestrictionType.SIMPLE, ctx);
                var propertyCompConfig = new FormSelectConfiguration();
                propertyCompConfig.setTitle(adminL10nFactory.Property());
                propertyCompConfig.getOptions().addAll(propertiesMetadata.stream().map(it2 -> {
                    var opt = new Option();
                    opt.setId(it2.propertyId);
                    opt.setDisplayName(it2.title.toString(LocaleUtils.getCurrentLocale()));
                    return opt;
                }).sorted(Comparator.comparing(Option::getDisplayName)).toList());
                propertyCompConfig.setValue(it.getPropertyId() == null ? null : it.getPropertyId());
                propertyCompConfig.setValueChangeListener(((oldValue, newValue, context) -> {
                    createCondition(child, newValue, null, null, context);
                }));
                var prop = new FormSelect("property", propertyCompConfig, ctx);
                child.addChild(ctx, prop, 0);
                children.add(child);
                createCondition(child, it.getPropertyId(), it.getConditionId(), it.getValue(), ctx);
                return;
            }
            child.setRestrictionType(it.getRestrictionType() == null? com.gridnine.platform.elsa.admin.web.common.RestrictionType.SIMPLE: com.gridnine.platform.elsa.admin.web.common.RestrictionType.valueOf(it.getRestrictionType().name()), ctx);
            var nestedChildren = new ArrayList<BaseUiElement>();
            collectChildren(nestedChildren, it.getNestedRestrictions(), ctx);
            for(var n = 0; n < nestedChildren.size(); n++){
                child.addChild(ctx, nestedChildren.get(n), n);
            }
            children.add(child);
        });
    }

    private void createCondition(RestrictionEditor child, String propertyId, String conditionId, Object value, OperationUiContext ctx) {
        var existingCondition  = child.findChildByTag("condition");
        if(existingCondition != null){
            child.removeChild(ctx, existingCondition);
        }
        if(propertyId == null){
            createValue(child, null, null, null, null, ctx);
            return;
        }
        var rendererId  = propertiesMetadata.stream().filter(it -> it.propertyId.equals(propertyId)).findFirst().get().rendererId;
        var renderer = renderersRegistry.getRestrictionRenderer(rendererId);
        var conditions = renderer.getConditions();

        var condCtrConfig = new FormSelectConfiguration();
        condCtrConfig.setTitle(adminL10nFactory.Condition());
        if(conditionId != null){
            condCtrConfig.setValue(conditionId);
        }
        condCtrConfig.setDeferred(false);
        condCtrConfig.getOptions().addAll(conditions.stream().map(it ->{
            var opt = new Option();
            opt.setId(it.getId());
            opt.setDisplayName(it.getDisplayName());
            return opt;
        }).toList());
        condCtrConfig.setValueChangeListener(((oldValue, newValue, context) -> {
            createValue(child, propertyId, newValue, null, rendererId, context);
        }));
        var condCtr = new FormSelect("condition", condCtrConfig, ctx);
        child.addChild(ctx, condCtr, 0);
        createValue(child, propertyId, conditionId, value, rendererId, ctx);
    }

    private void createValue(RestrictionEditor child, String propertyId, String conditionId, Object value, String rendererId, OperationUiContext context) {
        var existingValue  = child.findChildByTag("value");
        if(existingValue != null){
            child.removeChild(context, existingValue);
        }
        if(conditionId == null){
            return;
        }
        var renderer = renderersRegistry.getRestrictionRenderer(rendererId);
        var restriction = new Restriction();
        restriction.setValue(value);
        restriction.setRestrictionType(RestrictionType.SIMPLE);
        restriction.setConditionId(conditionId);
        restriction.setPropertyId(propertyId);
        var elm = ExceptionUtils.wrapException(()->renderer.createValueUiElement(null, restriction, "value", context));
        child.addChild(context, elm, 0);
    }


    @Override
    protected RestrictionsEditorConfiguration createConfiguration() {
        var result = new RestrictionsEditorConfiguration();
        return result;
    }

    public List<RestrictionPropertyMetadata> getPropertiesMetadata() {
        return propertiesMetadata;
    }

    public RestrictionsValueWrapper getData() {
        var result = new RestrictionsValueWrapper();
        collectData(result.getRestrictions(), getUnmodifiableListOfChildren());
        return result;
    }

    private void collectData(List<Restriction> restrictions, List<BaseUiElement> children) {
        children.forEach(child -> {
            var re = (RestrictionEditor) child;
            if(re.getRestrictionType() == com.gridnine.platform.elsa.admin.web.common.RestrictionType.SIMPLE){
                var prop = re.findChildByTag("property");
                if(prop instanceof FormSelect property && property.getValue() != null){
                    var cond = re.findChildByTag("condition");
                    if(cond instanceof FormSelect cd && cd.getValue() != null){
                        var value = re.findChildByTag("value");
                        var rendererId = propertiesMetadata.stream().filter(it -> it.propertyId.equals(property.getValue())).findFirst().get().rendererId;
                        var renderer = renderersRegistry.getRestrictionRenderer(rendererId);
                        var restr = ExceptionUtils.wrapException(()->renderer.getData(property.getValue(), cd.getValue(), value));
                        if(restr != null){
                            restrictions.add(restr);
                        }
                    }
                }
                return;
            }
            var nestedRestrictions = new ArrayList<Restriction>();
            collectData(nestedRestrictions, re.getUnmodifiableListOfChildren());
            if(!nestedRestrictions.isEmpty()){
                var restriction = new  Restriction();
                restriction.setRestrictionType(RestrictionType.valueOf(re.getRestrictionType().name()));
                restriction.getNestedRestrictions().addAll(nestedRestrictions);
                restrictions.add(restriction);
            }
        });
    }


    public boolean validate(OperationUiContext ctx) {
        return validateInternal(getUnmodifiableListOfChildren(), ctx);
    }

    private boolean validateInternal(List<BaseUiElement> unmodifiableListOfChildren, OperationUiContext ctx) {
        return unmodifiableListOfChildren.stream().allMatch(child -> {
            var re = (RestrictionEditor) child;
            if(re.getRestrictionType() == com.gridnine.platform.elsa.admin.web.common.RestrictionType.SIMPLE){
                var prop = re.findChildByTag("property");
                if(prop instanceof FormSelect property && property.getValue() != null){
                    var cond = re.findChildByTag("condition");
                    if(cond instanceof FormSelect cd && cd.getValue() != null){
                        var value = re.findChildByTag("value");
                        var rendererId = propertiesMetadata.stream().filter(it -> it.propertyId.equals(property.getValue())).findFirst().get().rendererId;
                        var renderer = renderersRegistry.getRestrictionRenderer(rendererId);
                        return renderer.validate(cd.getValue(), value, ctx);
                    }
                }
                return true;
            }
            return validateInternal(re.getUnmodifiableListOfChildren(), ctx);
        });
    }

    public record RestrictionPropertyMetadata(String propertyId, String rendererId, Object renderersParams,
                                              Localizable title) {
    }


}