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
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.common.core.search.SortOrder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

import java.util.ArrayList;
import java.util.UUID;

public class TestOrganizationsSection extends BaseWebAppUiElement {

    private final TestSearchField searchField;

    private final TestEntityListTable organizationsList;

    private final Storage storage;

    public TestOrganizationsSection(String tag, OperationUiContext ctx) {
        super("account.organization.OrganizationsSection", tag, ctx);
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        var config = createConfiguration(ctx);
        searchField = new TestSearchField("searchField", config.getSearchField(), ctx);
        addChild(ctx, searchField, 0);
        organizationsList = new TestEntityListTable("organizationsList", config.getOrganizationsList(), ctx);
        addChild(ctx, organizationsList, 0);
        decorateWithListeners(ctx);
        organizationsList.sendPostProcessCommand(ctx, "refresh-data", null);
    }


    private void decorateWithListeners(OperationUiContext ctx) {
        searchField.setValueChangeListener((context)->{
            organizationsList.setLoading(false, context);
            organizationsList.sendCommand(context, "refresh-data", null);
        }, ctx);
        organizationsList.setActionListener((rowId, columnId, actionId, context) ->{
            if("edit".equals(actionId)){
                TestWebApp.lookup(this).navigate("/account/organizations/"+rowId, context);
                return;
            }
            if("delete".equals(actionId)){
                var dialog = new TestConfirmDeleteDialog("delete-dialog", context);
                var buttons = new ArrayList<TestDialogButton>();
                {
                    var conf = new TestDialogButtonConfiguration();
                    conf.setTitle("Delete");
                    var deleteButton = new TestDialogButton("ok", conf, context);
                    deleteButton.setClickListener(ctx2 ->{
                        var org = storage.loadAsset(Organization.class, UUID.fromString(rowId), true);
                        storage.deleteAsset(org);
                        this.refreshData(ctx2);
                        TestWebApp.lookup(this).closeDialog(ctx2);
                        TestWebApp.lookup(this).notify("Organization deleted", ctx2);
                    });
                    buttons.add(deleteButton);
                }
                {
                    var conf = new TestDialogButtonConfiguration();
                    conf.setTitle("Cancel");
                    var deleteButton = new TestDialogButton("cancel", conf, context);
                    deleteButton.setClickListener(ctx2 ->{
                        TestWebApp.lookup(this).closeDialog(ctx2);
                    });
                    buttons.add(deleteButton);
                }
                TestWebApp.lookup(this).showDialog(dialog, buttons, context);
                return;
            }
        });
        organizationsList.setChangeSortListener((context, sort) ->{
            refreshData(context);
        });
        organizationsList.setRefreshDataListener(this::refreshData);
    }

    private void refreshData(OperationUiContext context) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            //noops
        }
        var listSort = organizationsList.getSort();
        var assets=storage.searchAssets(Organization.class, new SearchQueryBuilder().addOrder(listSort.getFieldId(),  listSort.getDirection() == TestSortDirection.ASC? SortOrder.ASC: SortOrder.DESC)
                .freeText(searchField.getValue()).limit(10).build(), false);
        var data = assets.stream().map(it ->{
            var entity = new  TestOrganizationEntry();
            entity.setAddress(it.getAddress());
            entity.setName(it.getName());
            entity.setContacts(it.getContacts());
            entity.setId(it.getId().toString());
            if(it.getCountry() != null) {
                entity.setCountry(it.getCountry().getCaption());
            }
            entity.getMenu().add(new TestOption("edit", "Edit"));
            entity.getMenu().add(new TestOption("delete", "Delete"));
            return entity;
        }).toList();
        organizationsList.setData(data, context);
        organizationsList.setLoading(false, context);
    }

    private TestOrganizationsSectionConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestOrganizationsSectionConfiguration();
        var searchField = new TestSearchFieldConfiguration();
        searchField.setDebounceTime(300);
        searchField.setDeferred(true);
        result.setSearchField(searchField);

        var organizationsListConfig = new TestEntityListConfiguration<TestOrganizationEntry>();
        result.setOrganizationsList(organizationsListConfig);
        {
            var nameColumn = new TestEntityListColumnDescription();
            nameColumn.setId("name");
            nameColumn.setSortable(true);
            nameColumn.setTitle("Name");
            nameColumn.setType(TestEntityListColumnType.TEXT);
            organizationsListConfig.getColumns().add(nameColumn);
        }
        {
            var countryColumn = new TestEntityListColumnDescription();
            countryColumn.setId("country");
            countryColumn.setSortable(true);
            countryColumn.setTitle("Country");
            countryColumn.setType(TestEntityListColumnType.TEXT);
            organizationsListConfig.getColumns().add(countryColumn);
        }
        {
            var addressColumn = new TestEntityListColumnDescription();
            addressColumn.setId("address");
            addressColumn.setSortable(true);
            addressColumn.setTitle("Address");
            addressColumn.setType(TestEntityListColumnType.TEXT);
            organizationsListConfig.getColumns().add(addressColumn);
        }
        {
            var menuColumn = new TestEntityListColumnDescription();
            menuColumn.setId("menu");
            menuColumn.setSortable(false);
            menuColumn.setType(TestEntityListColumnType.MENU);
            organizationsListConfig.getColumns().add(menuColumn);
        }
        var sort = new TestSort();
        sort.setDirection(TestSortDirection.ASC);
        sort.setFieldId("name");
        organizationsListConfig.setSort(sort);
        organizationsListConfig.setData(new ArrayList<>());
        organizationsListConfig.setLoading(true);
        return result;
    }


}
