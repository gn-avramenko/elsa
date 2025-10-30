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

package com.gridnine.platform.elsa.demo.admin.country;

import com.gridnine.platform.elsa.admin.list.BaseAssetUiListHandler;
import com.gridnine.platform.elsa.admin.list.FieldHandler;
import com.gridnine.platform.elsa.admin.list.ListToolHandler;
import com.gridnine.platform.elsa.admin.web.entityList.EntityList;
import com.gridnine.platform.elsa.demo.admin.domain.Country;
import com.gridnine.platform.elsa.demo.admin.domain.CountryFields;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;

public class CountryUiListHandler extends BaseAssetUiListHandler<Country> {

    public CountryUiListHandler() {
        super(Country.class);
    }

    @Override
    protected String getSection() {
        return "countries";
    }

    @Override
    protected List<FieldHandler> getColumns() {
        return List.of(assetField(CountryFields.name.name));
    }

    @Override
    protected List<?> getTools() {
        return List.of(new AddCountryToolHandler());
    }

    static class AddCountryToolHandler implements ListToolHandler {

        @Override
        public String getIcon() {
            return "PlusCircleOutlined";
        }

        @Override
        public String getTooltip() {
            return "Add";
        }

        @Override
        public void onClicked(OperationUiContext context, EntityList entityList) throws Exception {
            System.out.println("Adding country");
        }
    }
}

