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

package com.gridnine.platform.elsa.demo.ui.account.manager;

import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Manager;
import com.gridnine.platform.elsa.demo.domain.ManagerFields;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.domain.OrganizationFields;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.demo.ui.account.organization.OrganizationAutocompleteConfiguration;
import com.gridnine.platform.elsa.demo.ui.account.organization.OrganizationAutocompleteGetDataResponse;
import com.gridnine.platform.elsa.demo.ui.app.WebApp;
import com.gridnine.platform.elsa.demo.ui.common.SearchFieldConfiguration;
import com.gridnine.platform.elsa.demo.ui.components.test.TestOption;
import com.gridnine.platform.elsa.demo.ui.manager.ManagersListConfiguration;
import com.gridnine.platform.elsa.demo.ui.manager.ManagersListRow;
import com.gridnine.platform.elsa.webApp.common.*;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ManagersSection extends ManagersSectionSkeleton{

    private final Storage storage;

    private int limit;

    public ManagersSection(String tag, OperationUiContext ctx){
        super(tag, ctx);
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        limit = 20;
        getManagersList().refreshData(ctx, true);
    }

    @Override
    protected ManagersSectionConfiguration createConfiguration() {
        var config = new ManagersSectionConfiguration();
        config.setFlexDirection(FlexDirection.COLUMN);
        {
            var searchField = new SearchFieldConfiguration();
            searchField.setDeferred(false);
            searchField.setDebounceTime(300);
            searchField.setValueChangeListener(((oldValue, newValue, context) -> {
                getManagersList().setLoading(true, context);
                getManagersList().refreshData(context, true);
            } ));
            config.setSearch(searchField);
        }
        {
            var managersListConfig = new ManagersListConfiguration();
            {
                var nameColumn = new EntityListColumnDescription();
                nameColumn.setId("name");
                nameColumn.setSortable(true);
                nameColumn.setTitle("Name");
                nameColumn.setColumnType(EntityListColumnType.TEXT);
                managersListConfig.getColumns().add(nameColumn);
            }
            {
                var countryColumn = new EntityListColumnDescription();
                countryColumn.setId("email");
                countryColumn.setSortable(true);
                countryColumn.setTitle("Email");
                countryColumn.setColumnType(EntityListColumnType.TEXT);
                managersListConfig.getColumns().add(countryColumn);
            }
            {
                var addressColumn = new EntityListColumnDescription();
                addressColumn.setId("contacts");
                addressColumn.setSortable(true);
                addressColumn.setTitle("Contacts");
                addressColumn.setColumnType(EntityListColumnType.TEXT);
                managersListConfig.getColumns().add(addressColumn);
            }
            {
                var menuColumn = new EntityListColumnDescription();
                menuColumn.setId("menu");
                menuColumn.setSortable(false);
                menuColumn.setColumnType(EntityListColumnType.MENU);
                managersListConfig.getColumns().add(menuColumn);
            }
            var sort = new Sort();
            sort.setSortOrder(SortOrder.ASC);
            sort.setField("name");
            managersListConfig.setSort(sort);
            managersListConfig.setLoading(true);
            managersListConfig.setActionListener((act, context) -> {
                if("edit".equals(act.getActionId())){
                    WebApp.lookup(ManagersSection.this).navigate("/account/managers/"+act.getRowId(), context);
                    return;
                }
                if("delete".equals(act.getActionId())){
                    System.out.println("delete");
//                    var dialog = new TestConfirmDeleteDialog("delete-dialog", context);
//                    var buttons = new ArrayList<TestDialogButton>();
//                    {
//                        var conf = new TestDialogButtonConfiguration();
//                        conf.setTitle("Delete");
//                        var deleteButton = new TestDialogButton("ok", conf, context);
//                        deleteButton.setClickListener(ctx2 ->{
//                            var org = storage.loadAsset(Organization.class, UUID.fromString(rowId), true);
//                            storage.deleteAsset(org);
//                            this.refreshData(ctx2);
//                            TestWebApp.lookup(this).closeDialog(ctx2);
//                            TestWebApp.lookup(this).notify("Organization deleted", ctx2);
//                        });
//                        buttons.add(deleteButton);
//                    }
//                    {
//                        var conf = new TestDialogButtonConfiguration();
//                        conf.setTitle("Cancel");
//                        var deleteButton = new TestDialogButton("cancel", conf, context);
//                        deleteButton.setClickListener(ctx2 ->{
//                            TestWebApp.lookup(this).closeDialog(ctx2);
//                        });
//                        buttons.add(deleteButton);
//                    }
//                    TestWebApp.lookup(this).showDialog(dialog, buttons, context);
                    return;
                }
            });
            managersListConfig.setLoadMoreListener((context) -> {
                limit+=10;
                getManagersList().setLoading(true, context);
                getManagersList().refreshData(context, true);
            });
            managersListConfig.setSortListener((sa, context) ->{
                getManagersList().setLoading(true, context);
                getManagersList().setSort(sa.getSort(), context);
                getManagersList().refreshData(context, true);
            });
            managersListConfig.setRefreshDataListener(this::refreshData);
            config.setManagersList(managersListConfig);
        }
        {
            var organizationConfig = new OrganizationAutocompleteConfiguration();
            organizationConfig.setDebounceTime(1000);
            organizationConfig.setLimit(10);
            organizationConfig.setValueChangeListener(((oldValue, newValue, context) -> {
                getManagersList().setLoading(true, context);
                getManagersList().refreshData(context, true);
            }));
            organizationConfig.setGetDataServiceHandler((request, context) -> {
                var limit = request.getLimit();
                var query = request.getQuery();
                var orgs = storage.searchAssets(Organization.class, new SearchQueryBuilder().limit(limit).orderBy(OrganizationFields.name, com.gridnine.platform.elsa.common.core.search.SortOrder.ASC).freeText(query).build(), false);
                var values = orgs.stream().map(it ->{
                    var opt = new Option();
                    opt.setId(it.getId().toString());
                    opt.setDisplayName(it.toString());
                    return opt;
                }).sorted(Comparator.comparing(Option::getDisplayName)).toList();
                var result = new OrganizationAutocompleteGetDataResponse();
                result.getItems().addAll(values);
                return result;
            });
            config.setOrganization(organizationConfig);
        }
        return config;
    }
    private void refreshData(OperationUiContext context) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            //noops
        }
        if(getOrganization().getValue() == null || getOrganization().getValue().isEmpty()){
            getManagersList().setData(List.of(), context);
            getManagersList().setLoading(false, context);
            return;
        }
        var listSort = getManagersList().getSort();
        var org= getOrganization().getValue().get(0);
        var assets=storage.searchAssets(Manager.class, new SearchQueryBuilder().where(SearchCriterion.eq(ManagerFields.organization,
                        new EntityReference<>(UUID.fromString(org.getId()), Organization.class, null))).addOrder(listSort.getField(),  listSort.getSortOrder() == SortOrder.ASC? com.gridnine.platform.elsa.common.core.search.SortOrder.ASC: com.gridnine.platform.elsa.common.core.search.SortOrder.DESC)
                .freeText(getSearch().getValue()== null? null: getSearch().getValue()).limit(limit).build(), false);
        var data = assets.stream().map(it ->{
            var entity = new ManagersListRow();
            entity.setEmail(it.getEmail());
            entity.setName(it.getName());
            entity.setContacts(it.getContacts());
            entity.setId(it.getId().toString());
            {
                var opt = new Option();
                opt.setId("edit");
                opt.setDisplayName("Edit");
                entity.getMenu().add(opt);
            }
            {
                var opt = new Option();
                opt.setId("delete");
                opt.setDisplayName("Delete");
                entity.getMenu().add(opt);
            }
            return entity;
        }).toList();
        getManagersList().setData(data, context);
        getManagersList().setLoading(false, context);

    }
}