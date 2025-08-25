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

import com.gridnine.platform.elsa.webApp.BaseWebAppUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

public class TestOrganizationsSection extends BaseWebAppUiElement {

    private TestSearchField searchField;
    public TestOrganizationsSection(String tag, OperationUiContext ctx) {
        super("account.organization.OrganizationsSection", tag, ctx);
        var config = createConfiguration(ctx);
        searchField = new TestSearchField("searchField", config.getSearchField(), ctx);
        addChild(ctx, searchField, 0);
        decorateWithListeners(ctx);
    }


    private void decorateWithListeners(OperationUiContext ctx) {
        searchField.setValueChangeListener((arg) ->{
            System.out.println("searchText" + searchField.getValue());
        }, ctx);
    }
    private TestOrganizationsSectionConfiguration createConfiguration(OperationUiContext ctx) {
        var result = new TestOrganizationsSectionConfiguration();
        var searchField = new TestSearchFieldConfiguration();
        searchField.setDebounceTime(300);
        searchField.setDeferred(true);
        result.setSearchField(searchField);
//
//        var organizationsListConfig = new TestEntityListConfiguration();
//        result.setOrganizationsList(organizationsListConfig);
//        {
//            var nameColumn = new TestEntityListColumnDescription();
//            nameColumn.setId("name");
//            nameColumn.setSortable(true);
//            nameColumn.setTitle("Name");
//            nameColumn.setType(TestEntityListColumnType.TEXT);
//            organizationsListConfig.getColumns().add(nameColumn);
//        }
//        {
//            var countryColumn = new TestEntityListColumnDescription();
//            countryColumn.setId("country");
//            countryColumn.setSortable(true);
//            countryColumn.setTitle("Country");
//            countryColumn.setType(TestEntityListColumnType.TEXT);
//            organizationsListConfig.getColumns().add(countryColumn);
//        }
//        {
//            var addressColumn = new TestEntityListColumnDescription();
//            addressColumn.setId("address");
//            addressColumn.setSortable(true);
//            addressColumn.setTitle("Address");
//            addressColumn.setType(TestEntityListColumnType.TEXT);
//            organizationsListConfig.getColumns().add(addressColumn);
//        }
//        {
//            var menuColumn = new TestEntityListColumnDescription();
//            menuColumn.setId("menu");
//            menuColumn.setSortable(true);
//            menuColumn.setType(TestEntityListColumnType.MENU);
//            organizationsListConfig.getColumns().add(menuColumn);
//        }
//        var sort = new TestSort();
//        organizationsListConfig.setSort(sort);
//        organizationsListConfig.setData(new JsonArray());
        return result;
    }
}
