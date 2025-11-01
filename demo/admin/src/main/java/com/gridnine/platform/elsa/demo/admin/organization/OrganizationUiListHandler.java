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

package com.gridnine.platform.elsa.demo.admin.organization;

import com.gridnine.platform.elsa.admin.list.BaseAssetUiListHandler;
import com.gridnine.platform.elsa.admin.list.EntityListFilter;
import com.gridnine.platform.elsa.admin.list.FieldHandler;
import com.gridnine.platform.elsa.admin.list.ListToolHandler;
import com.gridnine.platform.elsa.admin.web.entityList.EntityList;
import com.gridnine.platform.elsa.demo.admin.domain.Organization;
import com.gridnine.platform.elsa.demo.admin.domain.OrganizationFields;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;

public class OrganizationUiListHandler extends BaseAssetUiListHandler<Organization> {

    public OrganizationUiListHandler() {
        super(Organization.class);
    }

    @Override
    protected String getSection() {
        return "organizations";
    }

    @Override
    protected List<FieldHandler> getColumns() {
        return List.of(assetField(OrganizationFields.name.name), assetField(OrganizationFields.address.name), assetField(OrganizationFields.country.name), assetField(OrganizationFields.contacts.name));
    }

    @Override
    protected List<?> getTools() {
        return List.of(new AddOrganizationToolHandler());
    }

    @Override
    protected List<EntityListFilter> getFilters(OperationUiContext context) throws Exception {
        return List.of(assetFilter(OrganizationFields.country.name, context));
    }

    @Override
    protected String getMobileRowContent(Organization asset) {
        return """
                <div class="rf">
                    <div class="lp">
                        <div class="b">
                            %s
                        </div>
                    </div>
                    <div class="rp">
                        <div class="st">
                            %s
                        </div>
                    </div>
                </div>
                """.formatted(asset.getName(), asset.getContacts());
    }

    static class AddOrganizationToolHandler implements ListToolHandler {

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
            System.out.println("Adding organization" + entityList.getSelectedItems());
        }
    }
}
