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

package com.gridnine.platform.elsa.admin.list;

import com.gridnine.platform.elsa.admin.domain.ListWorkspaceItem;
import com.gridnine.platform.elsa.admin.web.entityList.*;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.meta.common.StandardValueType;
import com.gridnine.platform.elsa.common.meta.domain.DatabaseCollectionType;
import com.gridnine.platform.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseAssetUiListHandler<T extends BaseAsset> implements UiListHandler{
    private final Class<T> assetClass;

    @Autowired
    private DomainMetaRegistry domainMetaRegistry;

    @Autowired
    private Storage storage;

    public BaseAssetUiListHandler(Class<T> assetClass) {
        this.assetClass = assetClass;
    }

    @Override
    public String getListId() {
        return assetClass.getName();
    }

    @Override
    public String getLink(ListWorkspaceItem item) {
        return "/%s".formatted(getSection());
    }

    @Override
    public boolean canHandle(String path) {
        var idx = path.lastIndexOf('/');
        return (idx == 0 || idx == -1) && getSection().equals(path);
    }

    @Override
    public BaseUiElement createElement(String path, OperationUiContext context) throws Exception {
        var entityListConfiguration = new EntityListConfiguration();
        var columns = getColumns();
        for (var column: columns){
            var c = new ColumnDescription();
            c.setId(column.getId());
            c.setTitle(column.getTitle());
            c.setColumnType(column.getColumnType());
            c.setSortable(column.isSortable());
            entityListConfiguration.getColumns().add(c);
        }
        entityListConfiguration.setDefaultSort(getDefaultSort(columns));
        entityListConfiguration.setLimit(30);
        var entityList = new EntityList("content", entityListConfiguration, context);
        entityList.setLoadMoreListener((ctx)->{
            entityList.setLimit(entityList.getLimit()+30, ctx);
            entityList.refreshData(ctx, true);
        }, context);
        entityList.setGetDataServiceHandler(((request, ctx) -> {
            var query = new SearchQuery();
            query.setLimit(entityList.getLimit()+1);
            query.setFreeText(request.getFreeText());
            query.getOrders().put(request.getSort().getField(), com.gridnine.platform.elsa.common.core.search.SortOrder.valueOf(request.getSort().getOrder().name()));
            var items = storage.searchAssets(assetClass, query, false);
            var result = new EntityListGetDataResponse();
            int idx = 0;
            boolean hasMore = false;
            for(var item: items){
                idx++;
                if(idx == entityList.getLimit()+1){
                    hasMore = true;
                    break;
                }
                var rowData = new RowData();
                result.getData().add(rowData);
                rowData.setId(item.getId().toString());
                columns.forEach(c -> {
                    rowData.getFields().put(c.getId(), c.getValueStr(item));
                });
            }
            result.setHasMore(hasMore);
            return result;

        }), context);

        return entityList;
    }



    private Sort getDefaultSort(List<FieldHandler> columns) {
        var result = new Sort();
        result.setField(columns.get(0).getId());
        result.setOrder(SortOrder.ASC);
        return result;
    }

    public String getTitle(){
        var title = domainMetaRegistry.getAssets().get(assetClass.getName()).getDisplayNames().get(LocaleUtils.getCurrentLocale());
        return title;
    }


    protected abstract String getSection();

    protected abstract List<FieldHandler> getColumns();

    protected FieldHandler assetField(String fieldName){
        return assetField(fieldName, true);
    }
    protected FieldHandler assetField(String fieldName, boolean sortable){
        var ad = domainMetaRegistry.getAssets().get(assetClass.getName());
        var prop = ad.getProperties().get(fieldName);
        var coll = ad.getCollections().get(fieldName);
        return new FieldHandler() {
            @Override
            public String getId() {
                return fieldName;
            }

            @Override
            public String getTitle() {
                return prop == null? coll.getDisplayNames().get(LocaleUtils.getCurrentLocale()): prop.getDisplayNames().get(LocaleUtils.getCurrentLocale());
            }

            @Override
            public ColumnType getColumnType() {
                return prop == null? BaseAssetUiListHandler.this.getColumnType(coll.getElementType()): BaseAssetUiListHandler.this.getColumnType(prop.getType());
            }

            @Override
            public String getValueStr(BaseIntrospectableObject item) {
                var value  = item.getValue(fieldName) == null? null: item.getValue(fieldName);
                return value == null? "": value.toString();
            }

            @Override
            public boolean isSortable() {
                return sortable;
            }
        };
    }

    private ColumnType getColumnType(DatabaseCollectionType item){
        return switch (item){
            default -> ColumnType.STRING;
        };
    }

    private ColumnType getColumnType(DatabasePropertyType item){
        return switch (item){
            default -> ColumnType.STRING;
        };
    }

}
