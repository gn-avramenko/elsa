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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.admin.web.acl;

import com.gridnine.platform.elsa.admin.acl.AclEngine;
import com.gridnine.platform.elsa.admin.acl.AclMetadataElement;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.Comparator;
import java.util.List;

public class AclTreeEditor extends AclTreeEditorSkeleton{

    private final AclEntryEditor entryEditor;

	public AclTreeEditor(String tag, OperationUiContext ctx){
		super(tag, ctx);
        entryEditor = new AclEntryEditor("entry", ctx);
        addChild(ctx, entryEditor, 0);
	}

    @Override
    protected AclTreeEditorConfiguration createConfiguration() {
        return new AclTreeEditorConfiguration();
    }

    public void readData(AclMetadataElement rootNode, OperationUiContext context) {
        var rootNodeWrapper = new AclMetadataElementWrapper();
        convert(rootNode, rootNodeWrapper);
        rootNodeWrapper.getChildren().sort(Comparator.comparing(AclMetadataElementWrapper::getName));
        setRootEntry(rootNodeWrapper, context);
        setSelectedNodeId(AclEngine.ROOT_NODE_ID, context);
        entryEditor.setData(List.of(), context);
    }

    private void convert(AclMetadataElement rootNode, AclMetadataElementWrapper rootNodeWrapper) {
        rootNodeWrapper.setId(rootNode.getId());
        rootNodeWrapper.setName(rootNode.getName().toString(LocaleUtils.getCurrentLocale()));
        rootNode.getChildren().forEach(child -> {
            var childWrapper = new AclMetadataElementWrapper();
            convert(child, childWrapper);
            rootNodeWrapper.getChildren().add(childWrapper);
        });
    }
}