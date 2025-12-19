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
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.ListRestrictionsMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.RootNodeAclHandler;
import com.gridnine.platform.elsa.admin.common.BasicAclObject;
import com.gridnine.platform.elsa.admin.common.RestrictionsValueRenderer;
import com.gridnine.platform.elsa.admin.domain.*;
import com.gridnine.platform.elsa.admin.domain.RestrictionType;
import com.gridnine.platform.elsa.admin.web.common.*;
import com.gridnine.platform.elsa.admin.web.entityList.*;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;
import com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAnd2Arguments;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchQuery;
import com.gridnine.platform.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.common.meta.common.StandardValueType;
import com.gridnine.platform.elsa.common.meta.domain.DatabaseCollectionType;
import com.gridnine.platform.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class BaseAssetUiListHandler<T extends BaseAsset> implements UiListHandler, AclHandler<Void> {
    protected final Class<T> assetClass;

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Autowired
    protected DomainMetaRegistry domainMetaRegistry;

    @Autowired
    protected Storage storage;

    @Autowired
    protected AdminL10nFactory aL10nFactory;

    @Autowired
    protected Localizer localizer;

    @Autowired
    protected SupportedLocalesProvider supportedLocalesProvider;

    private SearchCriterion additionalCriterion;

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
            c.setTitle(column.getTitle().toString(LocaleUtils.getCurrentLocale()));
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
        });
        entityList.setData(Collections.emptyList(), context);
        var filters = getFilters(context);
        if (!filters.isEmpty()) {
            entityList.setRestoreCommittedFilterValuesListener((context1) -> {
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
                var buttonConfig = new ButtonConfiguration();
                buttonConfig.setTitle(aL10nFactory.Apply());
                buttonConfig.setClickListener((context1) -> {
                    filters.forEach(filter -> {
                        filter.commitValue(context1);
                    });
                    entityList.setLoading(true, context1);
                    entityList.hideFilters(context1, true);
                    entityList.refreshData(context1, true);
                });
                var button = new Button("apply", buttonConfig, context);
                buttonsPanels.addChild(context, button, 0);
            }
            {
                var buttonConfig = new ButtonConfiguration();
                buttonConfig.setTitle(aL10nFactory.Clear());
                buttonConfig.setClickListener((context1) -> {
                    filters.forEach(filter -> {
                        filter.reset(context1);
                        filter.commitValue(context1);
                    });
                    entityList.hideFilters(context1, true);
                    entityList.setLoading(true, context1);
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
            if(additionalCriterion != null) {
                query.getCriterions().add(additionalCriterion);
            }
            filters.forEach(filter -> {
                var crit = filter.getSearchCriterion();
                if (crit != null) {
                    query.getCriterions().add(crit);
                }
            });
            query.getOrders().put(entityList.getSort().getField(), com.gridnine.platform.elsa.common.core.search.SortOrder.valueOf(entityList.getSort().getOrder().name()));
            var items = searchAssets(query);
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
                if (action.getBreakPoint() == BreakPoint.MOBILE) {
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
        var buttonsComponents = new ArrayList<EntityListButton>();
        var idx = 0;
        for (var tool : getTools()) {
            idx++;
            if (tool instanceof Glue) {
                var glue = new com.gridnine.platform.elsa.admin.web.common.Glue("glue-%s".formatted(idx), context);
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
            var button = new EntityListButton(th.getId(), config, context);
            tools.addChild(context, button, idx - 1);
            buttonsComponents.add(button);
        }
        var aclObj = new AssetUiListAclObject();
        aclObj.setButtons(buttonsComponents);
        aclObj.setList(this);
        context.getParameter(StandardParameters.BEAN_FACTORY).getBean(AclEngine.class).applyAcl("%s.list".formatted(assetClass.getName()), aclObj, getAclGroups(), context);
        entityList.addChild(context, tools, 0);
        entityList.refreshData(context, true);
        return entityList;
    }

    protected List<T> searchAssets(SearchQuery query) {
        return storage.searchAssets(assetClass, query, false);
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
        var title = coll == null ? prop.getDisplayNames().get(LocaleUtils.getCurrentLocale()) : coll.getDisplayNames().get(LocaleUtils.getCurrentLocale());
        if (prop != null) {
            switch (prop.getType()) {
                case ENTITY_REFERENCE:
                    return new EntityFilter(fieldName, title, (Class) Class.forName(prop.getClassName()), storage, context);
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
            public Localizable getTitle() {
                return prop == null ? com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(coll.getDisplayNames()) : com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(prop.getDisplayNames());
            }

            @Override
            public ColumnType getColumnType() {
                return prop == null ? BaseAssetUiListHandler.this.getColumnType(coll.getElementType()) : BaseAssetUiListHandler.this.getColumnType(prop.getType());
            }

            @Override
            public SerializablePropertyType getValueType() {
                return prop == null? SerializablePropertyType.valueOf(coll.getElementType().name()): SerializablePropertyType.valueOf(prop.getType().name());
            }

            @Override
            public String getValueClassName() {
                return prop == null? coll.getElementClassName(): prop.getClassName();
            }

            @Override
            public String getValueStr(BaseIntrospectableObject item) {
                var value = item.getValue(fieldName) == null ? null : item.getValue(fieldName);
                return value == null ? "" : value.toString();
            }

            @Override
            public boolean isCollection() {
                return prop==null;
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
            public String getId() {
                return "new-item";
            }

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

    protected ListToolHandler tool(String id, String icon, String toolTip, RunnableWithExceptionAnd2Arguments<OperationUiContext, EntityList> handler) {
        return new ListToolHandler() {

            @Override
            public String getId() {
                return id;
            }

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

    protected SelectionType getSelectionType() {
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

    protected List<EntityListFilter> getFilters(OperationUiContext context) throws Exception {
        return List.of();
    }

    protected abstract String getMobileRowContent(T asset);

    @Override
    public String getDefaultBackUrl(String path) {
        return "/";
    }

    @Override
    public double getPriority() {
        return 1;
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, Void elementMetadata, AclEngine aclEngine) throws Exception {
        var asset = domainMetaRegistry.getAssets().get(assetClass.getName());
        var groupItem = new AclMetadataElement();
        {
            var locales = new ArrayList<>(supportedLocalesProvider.getSupportedLocales());
            if(locales.isEmpty()) {
                locales.add(Locale.ENGLISH);
            }
            var names = new HashMap<Locale, String>();
            locales.forEach(locale -> {
                var oldLocale = LocaleUtils.getCurrentLocale();
                try {
                    LocaleUtils.setCurrentLocale(locale);
                    names.put(locale, ExceptionUtils.wrapException(()->getTitle("/"+getSection(), null)));
                } finally {
                    LocaleUtils.setCurrentLocale(oldLocale);
                }
            });
            groupItem.setName(com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(names));
        }
        groupItem.setId(assetClass.getName());
        groupItem.setHandlerId(getClass().getName());
        groupItem.getActions().add(new AllActionsMetadata(localizer));
        aclEngine.addNode(RootNodeAclHandler.ROOT_NODE_ID, groupItem);
        var listItem = new AclMetadataElement();
        listItem.setName(AdminL10nFactory.ListMessage(), localizer);
        listItem.setId("%s.list".formatted(assetClass.getName()));
        listItem.setHandlerId(getClass().getName());
        listItem.getActions().add(new AllActionsMetadata(localizer));
        {
            List<RestrictionsEditor.RestrictionPropertyMetadata> props = new ArrayList<>();
            {
                var prop = new RestrictionsEditor.RestrictionPropertyMetadata("_id", "%s-%s".formatted(StandardValueType.ENTITY_REFERENCE, asset.getId()), null, com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(AdminL10nFactory.List_ObjectMessage(), localizer));
                props.add(prop);
            }
            getColumns().forEach(it ->{
               if(it.getValueType() != SerializablePropertyType.ENTITY_REFERENCE) {
                   //TODO process all types
                    return;
               }
                var prop = new RestrictionsEditor.RestrictionPropertyMetadata(it.getId(), "%s-%s".formatted(it.getValueType().name(), it.getValueClassName()), null, it.getTitle());
                props.add(prop);
            });
            listItem.getActions().add(new ListRestrictionsMetadata(localizer, new RestrictionsValueRenderer.RestrictionsValueParameters(Collections.unmodifiableList(props))));
        }
        aclEngine.addNode(groupItem.getId(), listItem);
        var toolsItem = new AclMetadataElement();
        toolsItem.setId("%s.list.tools".formatted(assetClass.getName()));
        toolsItem.setName(AdminL10nFactory.ToolsMessage(), localizer);
        toolsItem.setHandlerId(getClass().getName());
        toolsItem.getActions().add(new AllActionsMetadata(localizer));
        aclEngine.addNode(listItem.getId(), toolsItem);
        var tools = getTools();
        tools.stream().filter(it -> it instanceof ListToolHandler).forEach(ti -> {
            var tool = (ListToolHandler) ti;
            var item = new AclMetadataElement();
            var locales = new ArrayList<>(supportedLocalesProvider.getSupportedLocales());
            if(locales.isEmpty()) {
                locales.add(Locale.ENGLISH);
            }
            var names = new HashMap<Locale, String>();
            locales.forEach(locale -> {
                var oldLocale = LocaleUtils.getCurrentLocale();
                try {
                    LocaleUtils.setCurrentLocale(locale);
                    names.put(locale, tool.getTooltip());
                } finally {
                    LocaleUtils.setCurrentLocale(oldLocale);
                }
            });
            item.setId("%s.tools.%s".formatted(assetClass.getName(), tool.getId()));
            item.setName(com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(names));
            item.setHandlerId(getClass().getName());
            item.getActions().add(new AllActionsMetadata(localizer));
            aclEngine.addNode(toolsItem.getId(), item);
        });
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, Void metadata, AclEngine aclEngine) {
        //noops
    }

    @Override
    public void applyActions(AclObjectProxy obj, Void metadata, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
        if(obj.getId().endsWith(".list")){
            obj.getCurrentActions().putAll(parentActions);
            actions.forEach(action -> {
                if (action.getId().equals(AllActionsMetadata.ACTION_ID)) {
                    var value = ((BooleanValueWrapper) action.getValue()).isValue();
                    obj.getCurrentActions().put(AllActionsMetadata.ACTION_ID, value);
                    return;
                }
                obj.getCurrentActions().put(ListRestrictionsMetadata.ACTION_ID, ((RestrictionsValueWrapper) action.getValue()).getRestrictions());
            });
            return;
        }
        if(obj.getId().contains(".tools.")){
            var parentValue = Boolean.TRUE.equals(parentActions.get(AllActionsMetadata.ACTION_ID));
            actions.forEach(action -> {
                var value = ((BooleanValueWrapper) action.getValue()).isValue();
                if (!parentValue) {
                    obj.getCurrentActions().put(AllActionsMetadata.ACTION_ID, value);
                }
            });
            return;
        }
    }

    @Override
    public void mergeActions(AclObjectProxy obj, Void metadata) {
        if(obj.getId().endsWith(".list")){
            var firstTime = obj.getTotalActions().isEmpty();
            if (!Boolean.TRUE.equals(obj.getTotalActions().get(AllActionsMetadata.ACTION_ID))) {
                obj.getTotalActions().put(AllActionsMetadata.ACTION_ID, obj.getCurrentActions().get(AllActionsMetadata.ACTION_ID));
            }
            var currentRestrictions = (List<Restriction>) obj.getCurrentActions().get(ListRestrictionsMetadata.ACTION_ID);
            var totalRestrictions = (List<Restriction>) obj.getTotalActions().get(ListRestrictionsMetadata.ACTION_ID);
            if (firstTime) {
                if (currentRestrictions != null && !currentRestrictions.isEmpty()) {
                    obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
                }
                return;
            }
            if (totalRestrictions != null && !totalRestrictions.isEmpty()) {
                if (currentRestrictions == null || currentRestrictions.isEmpty()) {
                    obj.getTotalActions().remove(ListRestrictionsMetadata.ACTION_ID);
                    return;
                }
                var compoundRestriction = new Restriction();
                compoundRestriction.setRestrictionType(com.gridnine.platform.elsa.admin.domain.RestrictionType.OR);
                var restr1 = new Restriction();
                if (totalRestrictions.size() > 1) {
                    restr1.setRestrictionType(com.gridnine.platform.elsa.admin.domain.RestrictionType.AND);
                    restr1.getNestedRestrictions().addAll(totalRestrictions);
                } else {
                    restr1 = totalRestrictions.get(0);
                }
                var restr2 = new Restriction();
                if (currentRestrictions.size() > 1) {
                    restr2.setRestrictionType(RestrictionType.AND);
                    restr2.getNestedRestrictions().addAll(currentRestrictions);
                } else {
                    restr2 = currentRestrictions.get(0);
                }
                compoundRestriction.getNestedRestrictions().add(restr1);
                compoundRestriction.getNestedRestrictions().add(restr2);
                obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
                return;
            }
            if (currentRestrictions == null || currentRestrictions.isEmpty()) {
                obj.getTotalActions().remove(ListRestrictionsMetadata.ACTION_ID);
                return;
            }
            obj.getTotalActions().put(ListRestrictionsMetadata.ACTION_ID, currentRestrictions);
            return;
        }
        if(obj.getId().contains(".tools.")) {
            var act = Boolean.TRUE.equals(obj.getTotalActions().get(AllActionsMetadata.ACTION_ID));
            if (!act) {
                obj.getTotalActions().put(AllActionsMetadata.ACTION_ID, obj.getCurrentActions().get(AllActionsMetadata.ACTION_ID));
            }
            return;
        }
    }

    public void setAdditionalCriterion(SearchCriterion additionalCriterion) {
        this.additionalCriterion = additionalCriterion;
    }

    @Override
    public void applyResults(AclObjectProxy root, Object aclObject, Void metadata, AclEngine aclEngine, OperationUiContext  context) {
        if(root.getId().endsWith(".list")){
            if(aclObject instanceof BasicAclObject basicAclObject){
                basicAclObject.setAccessAllowed(Boolean.TRUE.equals(root.getTotalActions().get(AllActionsMetadata.ACTION_ID)));
                return;
            }
            var listObject = (AssetUiListAclObject) aclObject;
            List<Restriction> restrictions = (List<Restriction>) root.getTotalActions().get(ListRestrictionsMetadata.ACTION_ID);
            if (restrictions != null && !restrictions.isEmpty()) {
                var act = root.getAclElement().getActions().stream().filter(it -> it.getId().equals(ListRestrictionsMetadata.ACTION_ID)).findFirst().get();
                var crit = aclEngine.toSearchCriterion(restrictions, ((RestrictionsValueRenderer.RestrictionsValueParameters) act.getRendererParameters()).properties());
                listObject.getList().setAdditionalCriterion(crit);
            }
            root.getChildren().forEach(it -> {
                applyResults(it, aclObject, metadata, aclEngine, context);
            });
            return;
        }
        if(root.getId().endsWith(".tools")){
            root.getChildren().forEach(it -> {
                applyResults(it, aclObject, metadata, aclEngine, context);
            });
            return;
        }
        if(root.getId().contains(".tools.")){
            if(aclObject instanceof AssetUiListAclObject obj){
                var toolId = root.getId().substring(root.getId().lastIndexOf('.') + 1);
                var button = obj.getButtons().stream().filter(it2 -> it2.getTag().equals(toolId)).findFirst().get();
                button.setDisabled(!Boolean.TRUE.equals(root.getTotalActions().get(AllActionsMetadata.ACTION_ID)), context);
            }
            return;
        }

    }

    protected abstract List<List<AclEntry>> getAclGroups();
}
