package com.gridnine.platform.elsa.admin.editor;

import com.gridnine.platform.elsa.admin.AdminL10nFactory;
import com.gridnine.platform.elsa.admin.web.common.Glue;
import com.gridnine.platform.elsa.admin.web.entityEditor.EditorTool;
import com.gridnine.platform.elsa.admin.web.entityEditor.EditorToolConfiguration;
import com.gridnine.platform.elsa.admin.web.entityEditor.EntityEditor;
import com.gridnine.platform.elsa.admin.web.entityEditor.EntityEditorToolType;
import com.gridnine.platform.elsa.admin.web.mainFrame.RouterPathHandler;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import com.gridnine.webpeer.core.utils.TypedParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseEditorRouterPathHandler<E extends BaseUiElement> implements RouterPathHandler {

    public static final TypedParameter<EntityEditor<?>> ENTITY_EDITOR = new TypedParameter<>("ENTITY_EDITOR");

    private final AtomicInteger index = new AtomicInteger(0);

    @Autowired
    private AdminL10nFactory adminL10nFactory;

    @Override
    public boolean canHandle(String path) {
        if(TextUtils.isBlank(path)){
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
        var id = path.split("/")[2];
        readData(result, id, context);
        return result;
    }

    protected abstract E createContent(String tag, OperationUiContext context) throws Exception;

    protected abstract void readData(EntityEditor<E> result, String id, OperationUiContext context) throws Exception;

    @Override
    public String getTitle(String path, OperationUiContext context) throws Exception {
        var editor = context.getParameter(ENTITY_EDITOR);
        return editor.getTitle();
    }

    @Override
    public String getDefaultBackUrl(String path) {
        return "/"+getSection();
    }

    protected BaseUiElement glue(OperationUiContext context) {
        return new Glue("glue-"+index.incrementAndGet(), context);
    }

    protected EditorTool deleteTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.ERROR);
        config.setIcon("DeleteOutlined");
        config.setTooltip(adminL10nFactory.Delete());
        config.setClickListener((ctx)->{
            System.out.println("delete");
        });
        return new EditorTool("delete", config, context);
    }

    protected EditorTool editTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.STANDARD);
        config.setIcon("EditOutlined");
        config.setTooltip(adminL10nFactory.Edit());
        config.setClickListener((ctx)->{
            System.out.println("edit");
        });
        return new EditorTool("edit", config, context);
    }

    protected EditorTool saveTool(EntityEditor<E> editor, OperationUiContext context) {
        var config = new EditorToolConfiguration();
        config.setButtonType(EntityEditorToolType.STANDARD);
        config.setIcon("SaveOutlined");
        config.setTooltip(adminL10nFactory.Save());
        config.setClickListener((ctx)->{
            System.out.println("save");
        });
        return new EditorTool("save", config, context);
    }

    protected abstract String getSection();
}
