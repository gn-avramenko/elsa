package com.gridnine.platform.elsa.admin.editor;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.web.common.Glue;
import com.gridnine.platform.elsa.admin.web.entityEditor.*;
import com.gridnine.platform.elsa.admin.web.mainFrame.MainFrame;
import com.gridnine.platform.elsa.admin.web.mainFrame.RouterPathHandler;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseEditorRouterPathHandler<E extends BaseUiElement> implements RouterPathHandler {

    public static final TypedParameter<EntityEditor<?>> ENTITY_EDITOR = new TypedParameter<>("ENTITY_EDITOR");

    private final AtomicInteger index = new AtomicInteger(0);

    @Autowired
    private AdminL10nFactory adminL10nFactory;

    @Autowired
    private Storage storage;

    @Autowired
    private ListableBeanFactory beanFactory;

    private volatile AclEngine aclEngine;

    protected AclEngine getAclEngine() {
       if(aclEngine == null) {
           synchronized (this) {
               if(aclEngine == null) {
                   aclEngine = beanFactory.getBean(AclEngine.class);
               }
           }
       }
       return aclEngine;
    }
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
        var content = createContent("content", context);
        result.setContent(content, context);
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
        readData(result, context);
        return result;
    }

    protected abstract E createContent(String tag, OperationUiContext context) throws Exception;

    protected abstract void readData(EntityEditor<E> result, OperationUiContext context) throws Exception;

    protected abstract WriteDataResult writeData(EntityEditor<E> result, OperationUiContext context) throws Exception;

    @Override
    public String getTitle(String path, OperationUiContext context) throws Exception {
        var editor = context.getParameter(ENTITY_EDITOR);
        return editor.getTitle();
    }

    @Override
    public String getDefaultBackUrl(String path) {
        return "/" + getSection();
    }

    protected BaseUiElement glue(OperationUiContext context) {
        return new Glue("glue-" + index.incrementAndGet(), context);
    }

    protected EditorTool deleteTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.ERROR);
        config.setIcon("DeleteOutlined");
        config.setTooltip(adminL10nFactory.Delete());
        config.setDisabledByDefault(false);
        config.getDisablingTags().add("new");
        config.setClickListener((ctx) -> {
            MainFrame.lookup(editor).confirm(adminL10nFactory.Are_you_sure_to_delete(), (ctx2)->{
               var asset = storage.loadAsset((Class)getObjectClass(), editor.getObjectId(), true);
               storage.deleteAsset(asset);
               MainFrame.lookup(editor).getMainRouter().navigate("/%s".formatted(getSection()), true, ctx2);
                MainFrame.lookup(editor).showInfo(adminL10nFactory.Object_deleted(), ctx2);
            }, ctx);
        });
        return new EditorTool("delete", config, context);
    }

    protected EditorTool editTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.STANDARD);
        var em = editor.getTags().contains("edit-mode");
        config.setIcon(em? "EyeOutlined": "EditOutlined");
        config.setTooltip(em? adminL10nFactory.View(): adminL10nFactory.Edit());
        config.getDisablingTags().add("has-changes");
        var tool = new EditorTool("edit", config, context);
        tool.setClickListener((ctx) -> {
            if (editor.getTags().contains("edit-mode")) {
                editor.removeTag("edit-mode", ctx);
                tool.setTooltip(adminL10nFactory.Edit(), ctx);
                tool.setIcon("EditOutlined", ctx);
                return;
            }
            editor.addTag("edit-mode", ctx);
            tool.setTooltip(adminL10nFactory.View(), ctx);
            tool.setIcon("EyeOutlined", ctx);
            return;
        });
        return tool;
    }

    protected EditorTool saveTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.STANDARD);
        config.setIcon("SaveOutlined");
        config.setTooltip(adminL10nFactory.Save());
        config.setDisabledByDefault(true);
        config.getEnablingTags().add("has-changes");
        config.setClickListener((ctx) -> {
            editor.removeTag("has-changes", ctx);
            var res = writeData(editor, ctx);
            if(res.success()) {
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
        });
        return new EditorTool("save", config, context);
    }

    protected abstract Class<?> getObjectClass();
    protected abstract String getSection();
}
