package com.gridnine.platform.elsa.admin.editor;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.acl.AclHandler;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.admin.acl.AclObjectProxy;
import com.gridnine.platform.elsa.admin.acl.standard.AllActionsMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.EditActionMetadata;
import com.gridnine.platform.elsa.admin.acl.standard.ViewActionMetadata;
import com.gridnine.platform.elsa.admin.domain.AclAction;
import com.gridnine.platform.elsa.admin.domain.AclEntry;
import com.gridnine.platform.elsa.admin.domain.BooleanValueWrapper;
import com.gridnine.platform.elsa.admin.web.entityEditor.*;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.admin.web.mainFrame.RouterPathHandler;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.core.l10n.SupportedLocalesProvider;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.common.meta.adminUi.AdminUiMetaRegistry;
import com.gridnine.platform.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseEditorRouterPathHandler<E extends BaseUiElement> implements RouterPathHandler, AclHandler<Void> {

    public static final TypedParameter<EntityEditor<?>> ENTITY_EDITOR = new TypedParameter<>("ENTITY_EDITOR");

    private final AtomicInteger index = new AtomicInteger(0);

    @Autowired
    private SupportedLocalesProvider  supportedLocalesProvider;

    @Autowired
    private AdminL10nFactory adminL10nFactory;

    @Autowired
    private Storage storage;

    @Autowired
    private ListableBeanFactory beanFactory;

    @Autowired
    private AdminUiMetaRegistry adminUiMetaRegistry;

    @Autowired
    private Localizer localizer;

    private AclEngine aclEngine;

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Autowired
    private DomainMetaRegistry  domainMetaRegistry;

    @Override
    public boolean canHandle(String path) {
        if (TextUtils.isBlank(path)) {
            return false;
        }
        var parts = path.split("/");
        return parts.length == 2 && parts[0].equals(getSection());
    }

    @Override
    public BaseUiElement createElement(String path, OperationUiContext context) throws Exception {
        var result = new EntityEditor<E>(context);
        context.setParameter(ENTITY_EDITOR, result);
        var correctedPath = path;
        if(correctedPath.contains("?")){
            correctedPath = correctedPath.substring(0,correctedPath.indexOf("?"));
        }
        var id = correctedPath.split("/")[2];
        result.setObjectId(id);
        if("new".equals(id)) {
            result.addTag("new", context);
        }
        if(path.contains("?editMode=true")){
            result.addTag("edit-mode", context);
        }
        var content = createContent("content", context);
        result.setContent(content, context);
        var tools = new ArrayList<BaseUiElement>();
        var elms = getTools(result.getTags().contains("edit-mode"));
        var toolsElements =  new ArrayList<EditorTool>();
       for(int n=0; n < elms.size(); n++){
           var elm = elms.get(n);
            if (elm instanceof com.gridnine.platform.elsa.admin.list.Glue) {
                var glue = new com.gridnine.platform.elsa.admin.web.common.Glue("glue-%s".formatted(n), context);
                tools.add(glue);
                continue;
            }
            var toolHandler = (EditorToolHandler<E>) elm;
           var config = new EditorToolConfiguration();
           config.setButtonType(toolHandler.getButtonType());
           config.setIcon(toolHandler.getIcon());
           config.getEnablingTags().addAll(toolHandler.getEnablingTags());
           config.getDisablingTags().addAll(toolHandler.getDisablingTags());
           config.setDisabledByDefault(toolHandler.isDisabledByDefault());
           var tool = new EditorTool(toolHandler.getId(), config, context);
           tool.setClickListener((ctx)->{
               toolHandler.onClicked(ctx, tool, result);
           });
            tools.add(tool);
            toolsElements.add(tool);
        }
        var aclObject = new UiEditorAclObject();
       aclObject.setEditor(result.getContent());
       aclObject.setTools(toolsElements);
       context.getParameter(StandardParameters.BEAN_FACTORY).getBean(AclEngine.class).applyAcl("%s.editor".formatted(getObjectClass().getName()), aclObject,getAcl(), context);
        result.setTools(tools, context);
        readData(result, context);
        return result;
    }

    protected abstract Class<E> getEditorClass();

    protected abstract E createContent(String tag, OperationUiContext context) throws Exception;

    protected abstract List<?> getTools(boolean editMode) throws Exception;

    protected abstract void readData(EntityEditor<E> result, OperationUiContext context) throws Exception;

    protected abstract WriteDataResult writeData(EntityEditor<E> result, OperationUiContext context) throws Exception;

    protected abstract List<List<AclEntry>> getAcl();

    @Override
    public String getTitle(String path, OperationUiContext context) throws Exception {
        var editor = context.getParameter(ENTITY_EDITOR);
        return editor.getTitle();
    }

    @Override
    public String getDefaultBackUrl(String path) {
        return "/" + getSection();
    }

    protected com.gridnine.platform.elsa.admin.list.Glue glue() {
        return new com.gridnine.platform.elsa.admin.list.Glue(){};
    }

    protected EditorToolHandler<E> deleteTool() {
        return new EditorToolHandler<E>() {
            @Override
            public String getId() {
                return "delete";
            }

            @Override
            public String getIcon() {
                return "DeleteOutlined";
            }

            @Override
            public Localizable getDescription() {
                return com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(AdminL10nFactory.DeleteMessage(), localizer);
            }

            @Override
            public void onClicked(OperationUiContext context, EditorTool tool, EntityEditor<E> editor) throws Exception {
                MainFrame.lookup(editor).confirm(adminL10nFactory.Are_you_sure_to_delete(), (ctx2)->{
                    var asset = storage.loadAsset((Class)getObjectClass(), editor.getObjectId(), true);
                    storage.deleteAsset(asset);
                    MainFrame.lookup(editor).getMainRouter().navigate("/%s".formatted(getSection()), true, ctx2);
                    MainFrame.lookup(editor).showInfo(adminL10nFactory.Object_deleted(), ctx2);
                }, context);
            }

            @Override
            public EntityEditorToolType getButtonType() {
                return EntityEditorToolType.ERROR;
            }
        };
    }

    protected EditorToolHandler<E> editTool(boolean defaultEditMode) {
        return new EditorToolHandler<E>() {
            @Override
            public String getId() {
                return "edit";
            }

            @Override
            public String getIcon() {
                return defaultEditMode? "EyeOutlined": "EditOutlined";
            }

            @Override
            public Localizable getDescription() {
                return defaultEditMode? com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(AdminL10nFactory.ViewMessage(),localizer): com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(AdminL10nFactory.EditMessage(), localizer);
            }

            @Override
            public void onClicked(OperationUiContext context, EditorTool tool, EntityEditor<E> editor) throws Exception {
                if (editor.getTags().contains("edit-mode")) {
                    editor.removeTag("edit-mode", context);
                    tool.setTooltip(adminL10nFactory.Edit(), context);
                    tool.setIcon("EditOutlined", context);
                    return;
                }
                editor.addTag("edit-mode", context);
                tool.setTooltip(adminL10nFactory.View(), context);
                tool.setIcon("EyeOutlined", context);
            }

            @Override
            public EntityEditorToolType getButtonType() {
                return EntityEditorToolType.STANDARD;
            }
        };
    }

    protected EditorToolHandler<E> saveTool() {
        return new  EditorToolHandler<E>() {

            @Override
            public String getId() {
                return "save";
            }

            @Override
            public String getIcon() {
                return "SaveOutlined";
            }

            @Override
            public Localizable getDescription() {
                return com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(AdminL10nFactory.SaveMessage(), localizer);
            }

            @Override
            public void onClicked(OperationUiContext ctx, EditorTool tool, EntityEditor<E> editor) throws Exception {
                var res = writeData(editor, ctx);
                if(res.success()) {
                    editor.removeTag("has-changes", ctx);
                    editor.removeTag("new", ctx);
                    if("new".equals(editor.getObjectId())){
                        MainFrame.lookup(editor).getMainRouter().navigate("/%s/%s?editMode=true".formatted(getSection(), res.id()), true, ctx);
                    }
                    MainFrame.lookup(editor).setTitle(editor.getTitle(), ctx);
                    return;
                }
                var errorMessage = res.errorMessage();
                if(TextUtils.isBlank(errorMessage)){
                    errorMessage = adminL10nFactory.Editor_has_validation_errors();
                }
                MainFrame.lookup(editor).showWarning(errorMessage, ctx);
            }

            @Override
            public EntityEditorToolType getButtonType() {
                return EntityEditorToolType.STANDARD;
            }

            @Override
            public boolean isDisabledByDefault() {
                return true;
            }

            @Override
            public List<String> getEnablingTags() {
                return List.of("has-changes");
            }
        };
    }

    @Override
    public double getPriority() {
        return 2;
    }

    public AclEngine getAclEngine() {
        return aclEngine;
    }

    @Override
    public void updateAclMetadata(AclMetadataElement parent, Void elementMetadata, AclEngine aclEngine) throws Exception {
        this.aclEngine = aclEngine;
        var container = adminUiMetaRegistry.getContainers().get(getEditorClass().getName());
        if(container == null){
            return;
        }
        var groupItem = aclEngine.getNode(getObjectClass().getName());
        var objectItem = new AclMetadataElement();
        objectItem.setName(AdminL10nFactory.EditorMessage(), localizer);
        objectItem.setId("%s.editor".formatted(getObjectClass().getName()));
        objectItem.setHandlerId(getClass().getName());
        objectItem.getActions().add(new AllActionsMetadata(localizer));
        objectItem.getActions().add(new EditActionMetadata(localizer));
        objectItem.getActions().add(new ViewActionMetadata(localizer));
        aclEngine.addNode(groupItem.getId(), objectItem);
        var toolsItem = new AclMetadataElement();
        toolsItem.setId("%s.editor.tools".formatted(getObjectClass().getName()));
        toolsItem.setName(AdminL10nFactory.ToolsMessage(), localizer);
        toolsItem.setHandlerId(getClass().getName());
        toolsItem.getActions().add(new AllActionsMetadata(localizer));
        aclEngine.addNode(objectItem.getId(), toolsItem);
        ExceptionUtils.wrapException(() -> getTools(true)).stream().filter(it -> it instanceof EditorToolHandler<?>).forEach(ti -> {
            var tool = (EditorToolHandler<?>) ti;
            var item = new AclMetadataElement();
            var locales = new ArrayList<>(supportedLocalesProvider.getSupportedLocales());
            if(locales.isEmpty()) {
                locales.add(Locale.ENGLISH);
            }
            var names = new HashMap<Locale, String>();
            locales.forEach(locale -> {
                names.put(locale, tool.getDescription().toString(locale));
            });
            item.setId("%s.editor.tools.%s".formatted(getObjectClass().getName(), tool.getId()));
            item.setName(com.gridnine.platform.elsa.admin.utils.LocaleUtils.createLocalizable(names));
            item.setHandlerId(getClass().getName());
            item.getActions().add(new AllActionsMetadata(localizer));
            aclEngine.addNode(toolsItem.getId(), item);
        });
        var contentItem = new AclMetadataElement();
        contentItem.setId("%s.editor.content".formatted(getObjectClass().getName()));
        contentItem.setName(AdminL10nFactory.ContentMessage(), localizer);
        contentItem.setHandlerId(getClass().getName());
        contentItem.getActions().add(new AllActionsMetadata(localizer));
        contentItem.getActions().add(new EditActionMetadata(localizer));
        contentItem.getActions().add(new ViewActionMetadata(localizer));
        aclEngine.addNode(objectItem.getId(), contentItem);
        String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
        var elementHandler = aclEngine.getHandler(handlerId);
        if(elementHandler != null){
            ExceptionUtils.wrapException(()->elementHandler.updateAclMetadata(contentItem, container, aclEngine));
        }
    }

    @Override
    public void fillProperties(AclObjectProxy root, Object aclObject, AclEngine aclEngine) {
        if (root.getId().endsWith(".editor") || root.getId().endsWith(".editor.tools")) {
            root.getChildren().forEach(child -> {
                aclEngine.getHandler(child.getAclElement().getHandlerId()).fillProperties(child, aclObject, aclEngine);
            });
            return;
        }
        if(root.getId().endsWith(".editor.content") || root.getId().contains(".editor.tools.")){
            var container = adminUiMetaRegistry.getContainers().get(getEditorClass().getName());
            String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
            var elementHandler = aclEngine.getHandler(handlerId);
            ExceptionUtils.wrapException(() -> elementHandler.fillProperties(root, ((UiEditorAclObject) aclObject).getEditor(), aclEngine));
            return;
        }
    }

    protected abstract Class<?> getObjectClass();
    protected abstract String getSection();

    @Override
    public void applyActions(AclObjectProxy obj, List<AclAction> actions, AclEngine aclEngine, Map<String, Object> parentActions) {
        if (obj.getId().endsWith(".editor") || obj.getId().endsWith(".editor.tools") || obj.getId().contains(".editor.tools.")) {
            obj.getCurrentActions().putAll(parentActions);
            actions.forEach(action -> {
                var value = ((BooleanValueWrapper) action.getValue()).isValue();
                obj.getCurrentActions().put(AllActionsMetadata.ACTION_ID, value);
            });
            return;
        }
        if (obj.getId().endsWith(".editor.content")) {
            var parentValue = Boolean.TRUE.equals(parentActions.get(AllActionsMetadata.ACTION_ID));
            obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, parentValue);
            obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, parentValue);
            actions.forEach(action -> {
                var value = ((BooleanValueWrapper) action.getValue()).isValue();
                if (action.getId().equals(AllActionsMetadata.ACTION_ID)) {
                    obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
                    obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
                } else if (action.getId().equals(ViewActionMetadata.ACTION_ID)) {
                    obj.getCurrentActions().put(ViewActionMetadata.ACTION_ID, value);
                } else if (action.getId().equals(EditActionMetadata.ACTION_ID)) {
                    obj.getCurrentActions().put(EditActionMetadata.ACTION_ID, value);
                }
            });
            return;
        }
    }

    @Override
    public void mergeActions(AclObjectProxy root) {
        if (root.getId().endsWith(".editor") || root.getId().endsWith(".editor.tools") || root.getId().contains(".editor.tools.")) {
            var value = Boolean.TRUE.equals(root.getTotalActions().get(AllActionsMetadata.ACTION_ID));
            if (value) {
                return;
            }
            root.getTotalActions().putAll(root.getCurrentActions());
            return;
        }
        if (root.getId().endsWith(".editor.content")) {
            var view = Boolean.TRUE.equals(root.getTotalActions().get(ViewActionMetadata.ACTION_ID));
            if (!view) {
                root.getTotalActions().put(ViewActionMetadata.ACTION_ID, root.getCurrentActions().get(ViewActionMetadata.ACTION_ID));
            }
            var edit = Boolean.TRUE.equals(root.getTotalActions().get(EditActionMetadata.ACTION_ID));
            if (!edit) {
                root.getTotalActions().put(EditActionMetadata.ACTION_ID, root.getCurrentActions().get(EditActionMetadata.ACTION_ID));
            }
            return;
        }
    }

    @Override
    public void applyResults(AclObjectProxy proxy, Object aclObject, Void metadata, AclEngine aclEngine, OperationUiContext  context) {
        if (proxy.getId().endsWith(".editor.content")) {
            var container = adminUiMetaRegistry.getContainers().get(getEditorClass().getName());
            String handlerId = "admin-ui-container-%s".formatted(container.getType().name());
            var elementHandler = aclEngine.getHandler(handlerId);
            ExceptionUtils.wrapException(() -> elementHandler.applyResults(proxy, ((UiEditorAclObject)aclObject).getEditor(), container, aclEngine, context));
            return;
        }
        if (proxy.getId().contains(".editor.tools.")) {
            var toolId = proxy.getId().substring(proxy.getId().lastIndexOf('.') + 1);
            if(aclObject instanceof UiEditorAclObject obj){
                var tool = obj.getTools().stream().filter(it  -> it.getTag().equals(toolId)).findFirst().get();
                tool.setDisabled(!Boolean.TRUE.equals(proxy.getTotalActions().get(AllActionsMetadata.ACTION_ID)), context);
            }
            return;
        }
        proxy.getChildren().forEach(it -> {
            applyResults(it, aclObject, metadata, aclEngine, context);
        });
    }
}
