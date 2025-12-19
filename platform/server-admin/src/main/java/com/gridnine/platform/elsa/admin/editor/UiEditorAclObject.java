package com.gridnine.platform.elsa.admin.editor;

import com.gridnine.platform.elsa.admin.list.BaseAssetUiListHandler;
import com.gridnine.platform.elsa.admin.web.entityEditor.EditorTool;
import com.gridnine.platform.elsa.admin.web.entityList.EntityListButton;
import com.gridnine.webpeer.core.ui.BaseUiElement;

import java.util.List;

public class UiEditorAclObject {
    private List<EditorTool> tools;
    private BaseUiElement editor;

    public List<EditorTool> getTools() {
        return tools;
    }

    public void setTools(List<EditorTool> tools) {
        this.tools = tools;
    }

    public BaseUiElement getEditor() {
        return editor;
    }

    public void setEditor(BaseUiElement editor) {
        this.editor = editor;
    }
}
