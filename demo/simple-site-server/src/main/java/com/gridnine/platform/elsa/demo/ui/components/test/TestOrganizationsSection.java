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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.WebPeerUtils;

public class TestOrganizationsSection extends BaseWebAppUiElement {

    private final TestSearchField searchField;

    private final TestEntityListTable organizationsList;

    public TestOrganizationsSection(String tag, OperationUiContext ctx, JsonObject state) {
        super("account.organization.OrganizationsSection", tag, ctx);
        var config = createConfiguration(ctx, state);
        searchField = new TestSearchField("searchField", config.getSearchField(), ctx);
        organizationsList = new TestEntityListTable("organizationsList", config.getOrganizationsList(), ctx);
        decorateWithListeners();
    }

    public TestSearchField getSearchField() {
        return searchField;
    }

    public TestEntityListTable getOrganizationsList() {
        return organizationsList;
    }

    private void decorateWithListeners() {

    }
    private TestOrganizationsSectionConfiguration createConfiguration(OperationUiContext ctx, JsonObject state) {
        var result = new TestOrganizationsSectionConfiguration();
        var textFieldConfig = new TestSearchFieldConfiguration();
        textFieldConfig.setDebounceTime(300);
        textFieldConfig.setValue(WebPeerUtils.getString(WebPeerUtils.getObject(state, "searchField"), "value"));
        result.setSearchField(textFieldConfig);
        var organizationsListConfig = new TestEntityListConfiguration();
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
            menuColumn.setSortable(true);
            menuColumn.setType(TestEntityListColumnType.MENU);
            organizationsListConfig.getColumns().add(menuColumn);
        }
        var sort = new TestSort();
        var sortObj = WebPeerUtils.getObject(WebPeerUtils.getObject(state, "organizationsList"), "sort");
        sort.setDirection(sortObj == null? TestSortDirection.ASC : TestSortDirection.valueOf(WebPeerUtils.getString(sortObj, "direction")));
        sort.setFieldId(sortObj == null? "name": WebPeerUtils.getString(sortObj, "name"));
        organizationsListConfig.setSort(sort);
        organizationsListConfig.setData(new JsonArray());
        return result;
    }
}
