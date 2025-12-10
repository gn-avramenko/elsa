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
    }

    public void setData(List<Restriction> restrictions, boolean readonly, OperationUiContext ctx) {
        setReadonly(readonly, ctx);
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
                propertyCompConfig.setReadonly(isReadonly());
                propertyCompConfig.setValueChangeListener(((oldValue, newValue, context) -> {
                    //TODO implement
                }));
                var prop = new FormSelect("property", propertyCompConfig, ctx);
                child.addChild(ctx, prop, 0);
                children.add(child);
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


    @Override
    protected RestrictionsEditorConfiguration createConfiguration() {
        var result = new RestrictionsEditorConfiguration();
        return result;
    }

    public List<RestrictionPropertyMetadata> getPropertiesMetadata() {
        return propertiesMetadata;
    }

    public RestrictionsValueWrapper getData() {
        return new RestrictionsValueWrapper();
    }

    public boolean validate(OperationUiContext ctx) {
        return true;
    }

    public record RestrictionPropertyMetadata(String propertyId, String rendererId, Object renderersParams,
                                              Localizable title) {
    }


}