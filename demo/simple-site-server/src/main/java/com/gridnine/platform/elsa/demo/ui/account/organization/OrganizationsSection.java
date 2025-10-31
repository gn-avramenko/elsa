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

import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Organization;
import com.gridnine.platform.elsa.demo.ui.SimpleSiteWebAppServlet;
import com.gridnine.platform.elsa.demo.ui.app.WebApp;
import com.gridnine.platform.elsa.demo.ui.common.SearchFieldConfiguration;
import com.gridnine.platform.elsa.demo.ui.organization.OrganizationsListConfiguration;
import com.gridnine.platform.elsa.demo.ui.organization.OrganizationsListRow;
import com.gridnine.platform.elsa.webApp.common.*;
import com.gridnine.webpeer.core.ui.OperationUiContext;


public class OrganizationsSection extends OrganizationsSectionSkeleton{

    private final Storage storage;

    private int limit;

	public OrganizationsSection(String tag, OperationUiContext ctx){
		super(tag, ctx);
        storage = ctx.getParameter(SimpleSiteWebAppServlet.BEAN_FACTORY).getBean(Storage.class);
        limit = 20;
        getOrganizationsList().refreshData(ctx, true);
	}

    @Override
    protected OrganizationsSectionConfiguration createConfiguration() {
        var config = new OrganizationsSectionConfiguration();
        config.setFlexDirection(FlexDirection.COLUMN);
        {
            var searchField = new SearchFieldConfiguration();
            searchField.setDeferred(false);
            searchField.setDebounceTime(300);
            searchField.setValueChangeListener(((oldValue, newValue, context) -> {
               getOrganizationsList().setLoading(true, context);
               getOrganizationsList().refreshData(context, true);
            } ));
            config.setSearch(searchField);
        }
        {
            var organizationsListConfig = new OrganizationsListConfiguration();
            {
                var nameColumn = new EntityListColumnDescription();
                nameColumn.setId("name");
                nameColumn.setSortable(true);
                nameColumn.setTitle("Name");
                nameColumn.setColumnType(EntityListColumnType.TEXT);
                organizationsListConfig.getColumns().add(nameColumn);
            }
            {
                var countryColumn = new EntityListColumnDescription();
                countryColumn.setId("country");
                countryColumn.setSortable(true);
                countryColumn.setTitle("Country");
                countryColumn.setColumnType(EntityListColumnType.TEXT);
                organizationsListConfig.getColumns().add(countryColumn);
            }
            {
                var addressColumn = new EntityListColumnDescription();
                addressColumn.setId("address");
                addressColumn.setSortable(true);
                addressColumn.setTitle("Address");
                addressColumn.setColumnType(EntityListColumnType.TEXT);
                organizationsListConfig.getColumns().add(addressColumn);
            }
            {
                var menuColumn = new EntityListColumnDescription();
                menuColumn.setId("menu");
                menuColumn.setSortable(false);
                menuColumn.setColumnType(EntityListColumnType.MENU);
                organizationsListConfig.getColumns().add(menuColumn);
            }
            var sort = new Sort();
            sort.setSortOrder(SortOrder.ASC);
            sort.setField("name");
            organizationsListConfig.setSort(sort);
            organizationsListConfig.setLoading(true);
            organizationsListConfig.setActionListener((act, context) -> {
                if("edit".equals(act.getActionId())){
                    WebApp.lookup(OrganizationsSection.this).navigate("/account/organizations/"+act.getRowId(), context);
                    return;
                }
                if("delete".equals(act.getActionId())){
                    WebApp.lookup(OrganizationsSection.this).confirm("Are you really want to delete?", (ctx2)->{
                        WebApp.lookup(OrganizationsSection.this).showInfo("Deleted", ctx2);
                    }, context);
                    return;
                }
            });
            organizationsListConfig.setLoadMoreListener((context) -> {
                limit+=10;
                getOrganizationsList().setLoading(true, context);
                getOrganizationsList().refreshData(context, true);
            });
            organizationsListConfig.setSortListener((sa, context) ->{
                getOrganizationsList().setLoading(true, context);
                getOrganizationsList().setSort(sa.getSort(), context);
                getOrganizationsList().refreshData(context, true);
            });
            organizationsListConfig.setRefreshDataListener(this::refreshData);
            config.setOrganizationsList(organizationsListConfig);
        }
        return config;
    }

    private void refreshData(OperationUiContext context) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            //noops
        }
        var listSort = getOrganizationsList().getSort();
        var assets=storage.searchAssets(Organization.class, new SearchQueryBuilder().addOrder(listSort.getField(),  listSort.getSortOrder() == SortOrder.ASC? com.gridnine.platform.elsa.common.core.search.SortOrder.ASC: com.gridnine.platform.elsa.common.core.search.SortOrder.DESC)
                .freeText(getSearch().getValue()== null? null: getSearch().getValue().getValue()).limit(limit).build(), false);
        var data = assets.stream().map(it ->{
            var entity = new OrganizationsListRow();
            entity.setAddress(it.getAddress());
            entity.setName(it.getName());
            entity.setContacts(it.getContacts());
            entity.setId(it.getId().toString());
            if(it.getCountry() != null) {
                entity.setCountry(it.getCountry().getCaption());
            }
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
        getOrganizationsList().setData(data, context);
        getOrganizationsList().setLoading(false, context);
    }
}