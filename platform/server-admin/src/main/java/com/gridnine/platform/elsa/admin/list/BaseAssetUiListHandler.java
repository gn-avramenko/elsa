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

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.domain.ListWorkspaceItem;
import com.gridnine.platform.elsa.admin.web.common.BreakPoint;
import com.gridnine.platform.elsa.admin.web.common.ButtonConfiguration;
import com.gridnine.platform.elsa.admin.web.common.ContentWrapperConfiguration;
import com.gridnine.platform.elsa.admin.web.entityList.*;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.meta.domain.DatabaseCollectionType;
import com.gridnine.platform.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.common.Button;
import com.gridnine.platform.elsa.webApp.common.ContentWrapper;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAssetUiListHandler<T extends BaseAsset> implements UiListHandler {
    protected final Class<T> assetClass;

    @Autowired
    protected DomainMetaRegistry domainMetaRegistry;

    @Autowired
    protected Storage storage;

    @Autowired
    protected AdminL10nFactory aL10nFactory;

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
        entityListConfiguration.setHasMore(false);
        var columns = getColumns();
        var sort = new Sort();
        sort.setOrder(SortOrder.ASC);
        sort.setField(columns.get(0).getId());
        entityListConfiguration.setSort(sort);
        for (var column : columns) {
            var c = new ColumnDescription();
            c.setId(column.getId());
            c.setTitle(column.getTitle());
            c.setColumnType(column.getColumnType());
            c.setSortable(column.isSortable());
            entityListConfiguration.getColumns().add(c);
        }
        entityListConfiguration.setLoading(true);
        entityListConfiguration.setSelectionType(getSelectionType());
        var searchFieldConfiguration = new EntityListSearchFieldConfiguration();
        searchFieldConfiguration.setDebounceTime(300);
        searchFieldConfiguration.setDeferred(false);
        searchFieldConfiguration.setValue(null);
        entityListConfiguration.setSearchField(searchFieldConfiguration);
        entityListConfiguration.setFiltersTitle(aL10nFactory.Filters());
        entityListConfiguration.setContentTitle(aL10nFactory.Content());

        var entityList = new EntityList("content", entityListConfiguration, context);
        entityList.setDoubleClickListener(((action, context1) -> {
            MainFrame.lookup(entityList).getMainRouter().navigate("/%s/%s".formatted(getSection(), action.getId()), true, context1);
        }));
        entityList.getSearchField().setValueChangeListener((oldValue, newValue, context1) -> {
            entityList.setLoading(true, context1);
            entityList.refreshData(context1, true);
        }, context);
        entityList.setLimit(30);
        entityList.setLoadMoreListener((ctx) -> {
            entityList.setLimit(entityList.getLimit() + 30);
            entityList.refreshData(ctx, true);
        });
        entityList.setChangeSortListener((action, ctx) -> {
            var newSort = new Sort();
            newSort.setOrder(action.getSortOrder());
            newSort.setField(action.getField());
            entityList.setSort(newSort, ctx);
            entityList.setLimit(30);
            entityList.setLoading(true, ctx);
            entityList.refreshData(ctx, true);
        } );
        entityList.setData(Collections.emptyList(), context);
        var filters = getFilters(context);
        if(!filters.isEmpty()){
            entityList.setRestoreCommittedFilterValuesListener((context1) ->{
                filters.forEach(filter -> {
                    filter.restoreCommitedValue(context1);
                });
            });
            var filtersPanels = new ContentWrapper("filters", new ContentWrapperConfiguration(), context);
            var filtersContainer = new ContentWrapper("filters", new ContentWrapperConfiguration(), context);
            filtersPanels.addChild(context, filtersContainer, 0);
            filters.forEach(filter -> {
                filtersContainer.addChild(context, filter.getElement(), 0);
            });
            entityList.addChild(context, filtersPanels, 0);
            var buttonsPanels = new ContentWrapper("buttons", new ContentWrapperConfiguration(), context);
            filtersPanels.addChild(context, buttonsPanels, 0);
            {
                var buttonConfig  = new ButtonConfiguration();
                buttonConfig.setTitle(aL10nFactory.Apply());
                buttonConfig.setClickListener((context1) -> {
                    filters.forEach(filter -> {
                        filter.commitValue(context1);
                    });
                    entityList.setLoading(true,  context1);
                    entityList.hideFilters(context1, true);
                    entityList.refreshData(context1, true);
                });
                var button = new Button("apply", buttonConfig, context);
                buttonsPanels.addChild(context, button, 0);
            }
            {
                var buttonConfig  = new ButtonConfiguration();
                buttonConfig.setTitle(aL10nFactory.Clear());
                buttonConfig.setClickListener((context1) -> {
                    filters.forEach(filter -> {
                        filter.reset(context1);
                        filter.commitValue(context1);
                    });
                    entityList.hideFilters(context1, true);
                    entityList.setLoading(true,  context1);
                    entityList.refreshData(context1, true);
                });
                var button = new Button("clear", buttonConfig, context);
                buttonsPanels.addChild(context, button, 0);
            }
        }
        entityList.setRefreshDataListener((action, ctx) -> {
            var query = new SearchQuery();
            query.setLimit(entityList.getLimit() + 1);
            query.setFreeText(entityList.getSearchField().getValue());
            filters.forEach(filter -> {
                var crit = filter.getSearchCriterion();
                if(crit != null){
                    query.getCriterions().add(crit);
                }
            });
            query.getOrders().put(entityList.getSort().getField(), com.gridnine.platform.elsa.common.core.search.SortOrder.valueOf(entityList.getSort().getOrder().name()));
            var items = storage.searchAssets(assetClass, query, false);
            var result = new ArrayList<RowData>();
            int idx = 0;
            boolean hasMore = false;
            for (var item : items) {
                idx++;
                if (idx == entityList.getLimit() + 1) {
                    hasMore = true;
                    break;
                }
                var rowData = new RowData();
                result.add(rowData);
                rowData.setId(item.getId().toString());
                if(action.getBreakPoint() == BreakPoint.MOBILE){
                    rowData.setMobileContent(getMobileRowContent(item));
                } else {
                    columns.forEach(c -> {
                        rowData.getFields().put(c.getId(), c.getValueStr(item));
                    });
                }
            }
            entityList.setHasMore(hasMore, ctx);
            entityList.setData(result, ctx);
            entityList.setLoading(false, ctx);
        });
        var tools = new ContentWrapper("tools", new ContentWrapperConfiguration(), context);
        var idx = 0;
        for (var tool : getTools()) {
            idx++;
            if (tool instanceof Glue) {
                var glue = new com.gridnine.platform.elsa.webApp.common.Glue("glue-%s".formatted(idx), context);
                tools.addChild(context, glue, idx - 1);
                continue;
            }
            var th = (ListToolHandler) tool;
            var config = new EntityListButtonConfiguration();
            config.setTitle(th.getTooltip());
            config.setIcon(th.getIcon());
            config.setClickListener((ctx) -> {
                th.onClicked(ctx, entityList);
            });
            var button = new EntityListButton("button-%s".formatted(idx), config, context);
            tools.addChild(context, button, idx - 1);
        }

        entityList.addChild(context, tools, 0);
        entityList.refreshData(context, true);
        return entityList;
    }


    private Sort getDefaultSort(List<FieldHandler> columns) {
        var result = new Sort();
        result.setField(columns.get(0).getId());
        result.setOrder(SortOrder.ASC);
        return result;
    }

    @Override
    public String getTitle(String path, OperationUiContext context) throws Exception {
        return domainMetaRegistry.getAssets().get(assetClass.getName()).getDisplayNames().get(LocaleUtils.getCurrentLocale());
    }

    protected abstract String getSection();

    protected abstract List<FieldHandler> getColumns();

    protected abstract List<?> getTools();

    protected FieldHandler assetField(String fieldName) {
        return assetField(fieldName, true);
    }

    protected EntityListFilter assetFilter(String fieldName, OperationUiContext context) throws ClassNotFoundException {
        var ad = domainMetaRegistry.getAssets().get(assetClass.getName());
        var prop = ad.getProperties().get(fieldName);
        var coll = ad.getCollections().get(fieldName);
        var title  = coll == null? prop.getDisplayNames().get(LocaleUtils.getCurrentLocale()): coll.getDisplayNames().get(LocaleUtils.getCurrentLocale());
        if(prop != null){
            switch (prop.getType()) {
                case ENTITY_REFERENCE:
                    return new EntityFilter(fieldName, title, (Class) Class.forName(prop.getClassName()),storage, context );
                default:
                    throw Xeption.forDeveloper("unsupported");
            }
        }
        throw Xeption.forDeveloper("unsupported");
    }

    protected FieldHandler assetField(String fieldName, boolean sortable) {
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
                return prop == null ? coll.getDisplayNames().get(LocaleUtils.getCurrentLocale()) : prop.getDisplayNames().get(LocaleUtils.getCurrentLocale());
            }

            @Override
            public ColumnType getColumnType() {
                return prop == null ? BaseAssetUiListHandler.this.getColumnType(coll.getElementType()) : BaseAssetUiListHandler.this.getColumnType(prop.getType());
            }

            @Override
            public String getValueStr(BaseIntrospectableObject item) {
                var value = item.getValue(fieldName) == null ? null : item.getValue(fieldName);
                return value == null ? "" : value.toString();
            }

            @Override
            public boolean isSortable() {
                return sortable;
            }
        };
    }

    protected Glue glue() {
        return new Glue() {
        };
    }

    protected ListToolHandler newItemTool() {
        return new ListToolHandler() {
            @Override
            public String getIcon() {
                return "PlusCircleOutlined";
            }

            @Override
            public String getTooltip() {
                return aL10nFactory.New_item();
            }

            @Override
            public void onClicked(OperationUiContext context, EntityList entityList) throws Exception {
                MainFrame.lookup(entityList).getMainRouter().navigate("/%s/new?editMode=true".formatted(getSection()), true, context);
            }
        };
    }
    protected ListToolHandler tool(String icon, String toolTip, RunnableWithExceptionAnd2Arguments<OperationUiContext, EntityList> handler) {
        return new ListToolHandler() {

            @Override
            public String getIcon() {
                return icon;
            }

            @Override
            public String getTooltip() {
                return toolTip;
            }

            @Override
            public void onClicked(OperationUiContext context, EntityList entityList) throws Exception {
                handler.run(context, entityList);
            }
        };
    }

    protected SelectionType getSelectionType(){
        return SelectionType.NONE;
    }
    private ColumnType getColumnType(DatabaseCollectionType item) {
        return switch (item) {
            default -> ColumnType.STRING;
        };
    }

    private ColumnType getColumnType(DatabasePropertyType item) {
        return switch (item) {
            default -> ColumnType.STRING;
        };
    }

    protected List<EntityListFilter> getFilters(OperationUiContext context) throws Exception{
        return List.of();
    }

    protected abstract String getMobileRowContent(T asset);

    @Override
    public String getDefaultBackUrl(String path) {
        return "/";
    }
}
