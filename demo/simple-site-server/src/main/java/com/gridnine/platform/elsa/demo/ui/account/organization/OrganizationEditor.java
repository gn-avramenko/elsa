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

package com.gridnine.platform.elsa.demo.ui.account.organization;

import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.common.core.search.SortOrder;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Country;
import com.gridnine.platform.elsa.demo.domain.CountryFields;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.demo.ui.app.WebApp;
import com.gridnine.platform.elsa.demo.ui.common.*;
import com.gridnine.platform.elsa.demo.ui.components.test.TestOption;
import com.gridnine.platform.elsa.demo.ui.components.test.TestWebApp;
import com.gridnine.platform.elsa.demo.ui.components.test.TestWebAppRouter;
import com.gridnine.platform.elsa.webApp.common.FlexDirection;
import com.gridnine.platform.elsa.webApp.common.Option;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.UUID;

public class OrganizationEditor extends OrganizationEditorSkeleton{

    private UUID organizationId;

    private Storage storage;

	public OrganizationEditor(String tag, OperationUiContext ctx){
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        String path = ctx.getParameter(TestWebAppRouter.ROUTER_PATH);
        organizationId = UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
        super(tag, ctx);
	}

    @Override
    protected OrganizationEditorConfiguration createConfiguration() {
        var org = storage.loadAsset(Organization.class, organizationId,false);
        var config = new OrganizationEditorConfiguration();
        config.setFlexDirection(FlexDirection.COLUMN);
        {
            var address = new StandardTextFieldConfiguration();
            address.setDeferred(true);
            address.setValue(org.getAddress());
            config.setAddress(address);
        }
        {
            var contacts = new StandardTextFieldConfiguration();
            contacts.setDeferred(true);
            contacts.setValue(org.getContacts());
            config.setContacts(contacts);
        }
        {
            var name = new StandardTextFieldConfiguration();
            name.setDeferred(true);
            name.setValue(org.getName());
            config.setName(name);
        }
        {
            var country = new CountryAutocompleteFieldConfiguration();
            country.setDeferred(true);
            country.setDebounceTime(300);
            country.setLimit(10);
            country.setMultiple(false);
            if(org.getCountry() != null){
                var opt = new Option();
                opt.setId(org.getCountry().getId().toString());
                opt.setDisplayName(org.getCountry().toString());
                country.getValue().add(opt);
            }
            country.setGetDataServiceHandler((request, context) -> {
                var limit = request.getLimit();
                var query = request.getQuery();
                var countries = storage.searchAssets(Country.class, new SearchQueryBuilder().limit(limit).orderBy(CountryFields.name, SortOrder.ASC).freeText(query).build(), false);
                var items = countries.stream().map(it ->{
                    var option = new Option();
                    option.setId(it.getId().toString());
                    option.setDisplayName(it.toString());
                    return option;
                }).toList();
                var result = new CountryAutocompleteFieldGetDataResponse();
                result.getItems().addAll(items);
                return result;
            });
            config.setCountry(country);
        }
        {
            var result = new EditorTitleLabelConfiguration();
            result.setTitle(org.getName());
            config.setTitle(result);
        }
        {
            var result = new EditorBackButtonConfiguration();
            result.setTitle("Back");
            result.setClickListener(context ->{
                WebApp.lookup(OrganizationEditor.this).navigate("/account/organizations", context);
            });
            config.setBackButton(result);
        }
        {
            var saveButton = new EditorSaveButtonConfiguration();
            saveButton.setTitle("Save");
            saveButton.setDisabled(true);
            saveButton.setClickListener((context)->{
                boolean hasErrors = false;
                getName().setValidationMessage(null, context);
                getContacts().setValidationMessage(null, context);
                getAddress().setValidationMessage(null, context);
                getCountry().setValidationMessage(null, context);
                if(getName().getValue() == null || TextUtils.isBlank(getName().getValue())){
                    hasErrors = true;
                    getName().setValidationMessage("Field is blank", context);
                }
                if(getContacts().getValue() == null || TextUtils.isBlank(getContacts().getValue())){
                    hasErrors = true;
                    getContacts().setValidationMessage("Field is blank", context);
                }
                if(getAddress().getValue() == null || TextUtils.isBlank(getAddress().getValue())){
                    hasErrors = true;
                    getAddress().setValidationMessage("Field is blank", context);
                }
                if(getCountry().getValue().isEmpty()){
                    hasErrors = true;
                    getCountry().setValidationMessage("Field is blank", context);
                }
                if(hasErrors){
                    return;
                }
                var cnt = storage.loadAsset(Organization.class, organizationId, true);
                cnt.setContacts(getContacts().getValue());
                cnt.setAddress(getAddress().getValue());
                cnt.setName(getName().getValue());
                var opt = getCountry().getValue().get(0);
                cnt.setCountry(new EntityReference<>(UUID.fromString(opt.getId()), Country.class, opt.getDisplayName()));
                storage.saveAsset(cnt, "editor");
                resetChanges(context, false);
//              (false, ctx);
//              TestWebApp.lookup(this).notify("Organization saved", ctx);
                return;
            });
            config.setSaveButton(saveButton);
        }
        return config;
    }
}