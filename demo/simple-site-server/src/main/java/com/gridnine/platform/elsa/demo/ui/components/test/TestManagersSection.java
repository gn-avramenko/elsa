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

import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.common.core.search.SortOrder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Manager;
import com.gridnine.platform.elsa.demo.domain.ManagerFields;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.domain.OrganizationFields;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.ArrayList;
import java.util.UUID;

public class TestManagersSection extends BaseWebAppUiElement {

    private final TestSearchField searchField;

    private final TestEntityListTable managersList;

    private final TestAutocompleteField organizationField;

    private final Storage storage;

    public TestManagersSection(String tag, OperationUiContext ctx) {
        super("account.manager.ManagersSection", tag, ctx);
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        var config = createConfiguration(ctx);
        searchField = new TestSearchField("searchField", config.getSearchField(), ctx);
        addChild(ctx, searchField, 0);
        managersList = new TestEntityListTable("managersList", config.getManagersList(), ctx);
        addChild(ctx, managersList, 0);
        organizationField = new TestAutocompleteField("organization", config.getOrganization(), ctx);
        addChild(ctx, organizationField, 0);
        decorateWithListeners(ctx);
        managersList.sendPostProcessCommand(ctx, "refresh-data", null);
    }


    private void decorateWithListeners(OperationUiContext ctx) {
        searchField.setValueChangeListener((context)->{
            managersList.setLoading(false, context);
            managersList.sendCommand(context, "refresh-data", null);
        }, ctx);
        managersList.setActionListener((rowId, columnId, actionId, context) ->{
            System.out.printf("action: %s %s %s%n", rowId, columnId, actionId);
        });
        managersList.setChangeSortListener((context, sort) ->{
            refreshData(context);
        });
        managersList.setRefreshDataListener(this::refreshData);
        organizationField.setValueChangeListener((context)->{
            managersList.setLoading(true, context);
            managersList.sendPostProcessCommand(context, "refresh-data", null);
        }, ctx);
        organizationField.setSuggestionsProvider((context, request) ->{
            var limit = request.getLimit();
            var query = request.getQuery();
            var orgs = storage.searchAssets(Organization.class, new SearchQueryBuilder().limit(limit).orderBy(OrganizationFields.name, SortOrder.ASC).freeText(query).build(), false);
            return orgs.stream().map(it ->new TestOption(it.getId().toString(), it.toString())).toList();
        });
    }

    private void refreshData(OperationUiContext context) {
        var organizations = organizationField.getValues();
        if(organizations.isEmpty()){
            managersList.setData(new ArrayList<>(), context);
            managersList.setLoading(false, context);
            return;
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            //noops
        }
        var org = organizations.get(0);
        var organization = new EntityReference<>(UUID.fromString(org.getId()), Organization.class, org.getDisplayName());
        var listSort = managersList.getSort();
        var assets=storage.searchAssets(Manager.class, new SearchQueryBuilder()
                .addOrder(listSort.getFieldId(),  listSort.getDirection() == TestSortDirection.ASC? SortOrder.ASC: SortOrder.DESC).where(SearchCriterion.eq(ManagerFields.organization, organization))
                .freeText(searchField.getValue()).limit(10).build(), false);
        var data = assets.stream().map(it ->{
            var entity = new TestManagerEntry();
            entity.setName(it.getName());
            entity.setContacts(it.getContacts());
            entity.setId(it.getId().toString());
            entity.setEmail(it.getEmail());
            entity.getMenu().add(new TestOption("edit", "Edit"));
            entity.getMenu().add(new TestOption("delete", "Delete"));
            return entity;
        }).toList();
        managersList.setData(data, context);
        managersList.setLoading(false, context);
    }

    private TestManagersSectionConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestManagersSectionConfiguration();
        var searchField = new TestSearchFieldConfiguration();
        searchField.setDebounceTime(300);
        searchField.setDeferred(true);
        result.setSearchField(searchField);

        var managersListConfig = new TestEntityListConfiguration<TestManagerEntry>();
        result.setManagersList(managersListConfig);
        {
            var nameColumn = new TestEntityListColumnDescription();
            nameColumn.setId("name");
            nameColumn.setSortable(true);
            nameColumn.setTitle("Name");
            nameColumn.setType(TestEntityListColumnType.TEXT);
            managersListConfig.getColumns().add(nameColumn);
        }
        {
            var emailColumn = new TestEntityListColumnDescription();
            emailColumn.setId("email");
            emailColumn.setSortable(true);
            emailColumn.setTitle("Email");
            emailColumn.setType(TestEntityListColumnType.TEXT);
            managersListConfig.getColumns().add(emailColumn);
        }
        {
            var contactsColumn = new TestEntityListColumnDescription();
            contactsColumn.setId("contacts");
            contactsColumn.setSortable(true);
            contactsColumn.setTitle("Contacts");
            contactsColumn.setType(TestEntityListColumnType.TEXT);
            managersListConfig.getColumns().add(contactsColumn);
        }
        {
            var menuColumn = new TestEntityListColumnDescription();
            menuColumn.setId("menu");
            menuColumn.setSortable(false);
            menuColumn.setType(TestEntityListColumnType.MENU);
            managersListConfig.getColumns().add(menuColumn);
        }
        var sort = new TestSort();
        sort.setDirection(TestSortDirection.ASC);
        sort.setFieldId("name");
        managersListConfig.setSort(sort);
        managersListConfig.setData(new ArrayList<>());
        managersListConfig.setLoading(true);
        result.setOrganization(new TestAutocompleteFieldConfiguration());
        result.getOrganization().setDeferred(true);
        result.getOrganization().setDebounceTime(300);
        return result;
    }
}
