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
 */

package com.gridnine.platform.elsa.demo.ui.components.test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.common.core.search.SortOrder;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Country;
import com.gridnine.platform.elsa.demo.domain.CountryFields;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.List;
import java.util.UUID;

public class TestOrganizationEditor extends TestBaseEditor{

    private final TestStandardEditorTextField name;

    private final TestStandardEditorTextField contacts;

    private final TestStandardEditorTextField address;

    private final TestAutocompleteField country;

    private UUID organizationId;

    private Storage storage;

    public TestOrganizationEditor(String tag, OperationUiContext ctx) {
        super("account.organization.OrganizationEditor", tag, ctx);
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        var config = createConfiguration(ctx);
        setTitle(config.getTitle(), ctx);
        setHasChanges(config.isHasChanges(), ctx);
        name = new TestStandardEditorTextField("name", config.getName(), ctx);
        addChild(ctx, name, 0);
        address = new TestStandardEditorTextField("address", config.getAddress(), ctx);
        addChild(ctx, address, 0);
        contacts = new TestStandardEditorTextField("contacts", config.getName(), ctx);
        addChild(ctx, contacts, 0);
        country = new TestAutocompleteField("country", config.getCountry(), ctx);
        addChild(ctx, country, 0);
        decorateWithListeners(ctx);
        String path = ctx.getParameter(TestWebAppRouter.ROUTER_PATH);
        organizationId = UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
        var state = ctx.getParameter(OperationUiContext.INIT_STATE);
        if(state == null || state.getAsJsonObject().isEmpty()) {
            setDataLoading(config.isDataLoading(), ctx);
            sendPostProcessCommand(ctx, "load-data", null);
        }
    }

    private void decorateWithListeners(OperationUiContext ctx) {
        country.setSuggestionsProvider((context, request) ->{
            var limit = request.getLimit();
            var query = request.getQuery();
            var countries = storage.searchAssets(Country.class, new SearchQueryBuilder().limit(limit).orderBy(CountryFields.name, SortOrder.ASC).freeText(query).build(), false);
            return countries.stream().map(it ->new TestOption(it.getId().toString(), it.toString())).toList();
        });
    }

    private TestOrganizationConfiguration createConfiguration(OperationUiContext ctx) {
        var result =  new TestOrganizationConfiguration();
        result.setTitle("Organization");
        result.setDataLoading(true);
        result.setName(new TestStandardEditorTextFieldConfiguration());
        result.getName().setDeferred(true);
        result.setAddress(new TestStandardEditorTextFieldConfiguration());
        result.getAddress().setDeferred(true);
        result.setContacts(new TestStandardEditorTextFieldConfiguration());
        result.getContacts().setDeferred(true);
        result.setCountry(new TestAutocompleteFieldConfiguration());
        result.getCountry().setDeferred(true);
        result.getCountry().setDebounceTime(300);
        result.getCountry().setLimit(10);
        result.getCountry().setMultiple(false);
        return result;
    }

    @Override
    public void restoreFromState(JsonElement state, OperationUiContext ctx) {
        super.restoreFromState(state, ctx);
        setHasChanges(true, ctx);
    }

    @Override
    public void processCommand(OperationUiContext ctx, String commandId, JsonElement data) throws Exception {
        if(commandId.equals("load-data")){
            Thread.sleep(1000L);
            var org = storage.loadAsset(Organization.class, organizationId, false);
            setDataLoading(false, ctx);
            setTitle(org.getName(), ctx);
            name.setValue(org.getName(), ctx);
            contacts.setValue(org.getContacts(), ctx);
            address.setValue(org.getAddress(), ctx);
            var cnt = org.getCountry();
            if(cnt != null){
                country.setValues(List.of(new TestOption(cnt.getId().toString(), cnt.getCaption())), ctx);
            }
            return;
        }
        if(commandId.equals("back")){
            TestWebApp.lookup(this).navigate("/account/organizations", ctx);
            return;
        }
        if(commandId.equals("save")){
            boolean hasErrors = false;
            if(TextUtils.isBlank(name.getValue())){
                hasErrors = true;
                name.setValidationMessage("Field is blank", ctx);
            }
            if(TextUtils.isBlank(contacts.getValue())){
                hasErrors = true;
                contacts.setValidationMessage("Field is blank", ctx);
            }
            if(TextUtils.isBlank(address.getValue())){
                hasErrors = true;
                address.setValidationMessage("Field is blank", ctx);
            }
            if(country.getValues().isEmpty()){
                hasErrors = true;
                country.setValidationMessage("Field is blank", ctx);
            }
            if(hasErrors){
                return;
            }
            var cnt = storage.loadAsset(Organization.class, organizationId, true);
            cnt.setContacts(contacts.getValue());
            cnt.setAddress(address.getValue());
            cnt.setName(name.getValue());
            var opt = country.getValues().get(0);
            cnt.setCountry(new EntityReference<>(UUID.fromString(opt.getId()), Country.class, opt.getDisplayName()));
            storage.saveAsset(cnt, "editor");
            setHasChanges(false, ctx);
            TestWebApp.lookup(this).notify("Organization saved", ctx);
            return;
        }
        super.processCommand(ctx, commandId, data);
    }
}
