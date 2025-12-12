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

package com.gridnine.platform.elsa.admin.editor;

import com.gridnine.platform.elsa.admin.web.entityEditor.EditorTool;
import com.gridnine.platform.elsa.admin.web.entityEditor.EntityEditor;
import com.gridnine.platform.elsa.admin.web.entityEditor.EntityEditorToolType;
import com.gridnine.platform.elsa.common.core.model.common.Localizable;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;

public interface EditorToolHandler<E extends BaseUiElement> {
    String getId();
    String getIcon();
    Localizable getDescription();
    void onClicked(OperationUiContext context, EditorTool tool, EntityEditor<E> editor) throws Exception;
    EntityEditorToolType getButtonType();
    default List<String> getDisablingTags() {
        return List.of();
    }
    default List<String> getEnablingTags() {
        return List.of();
    }
    default boolean isDisabledByDefault() {return false;
    }
}
