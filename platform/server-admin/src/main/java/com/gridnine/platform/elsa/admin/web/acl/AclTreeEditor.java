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
import com.gridnine.platform.elsa.admin.domain.AclEntry;
import com.gridnine.platform.elsa.common.core.utils.LocaleUtils;
import com.gridnine.platform.elsa.webApp.StandardParameters;
import com.gridnine.webpeer.core.ui.OperationUiContext;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.*;

public class AclTreeEditor extends AclTreeEditorSkeleton{

    private final AclEntryEditor entryEditor;
    private final Map<String, AclEntry> aclEntries = new HashMap<>();
    private final Map<String, AclMetadataElement> metadata = new HashMap<>();
    private final ListableBeanFactory  beanFactory;

	public AclTreeEditor(String tag, OperationUiContext ctx){
		super(tag, ctx);
        entryEditor = new AclEntryEditor("entry", ctx);
        beanFactory = ctx.getParameter(StandardParameters.BEAN_FACTORY);
        addChild(ctx, entryEditor, 0);
	}

    @Override
    protected AclTreeEditorConfiguration createConfiguration() {
        var result = new AclTreeEditorConfiguration();
        result.setSelectNodeListener((act ,ctx)->{
            ctx.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
            if(!entryEditor.validate(ctx)){
                return;
            }
            var entry = entryEditor.getData();
            if(entry == null){
                aclEntries.remove(getSelectedNodeId());
            } else {
                aclEntries.put(getSelectedNodeId(), entry);
            }
            setSelectedNodeId(act.getNodeId(), ctx);
            updateHasRules(ctx);
            var newEntry = aclEntries.get(act.getNodeId());
            entryEditor.setData(metadata.get(act.getNodeId()), newEntry == null? List.of(): newEntry.getRules(), ctx);
        });
        return result;
    }

    public boolean validate(OperationUiContext context){
        return entryEditor.validate(context);
    }

    public void readData(AclMetadataElement rootNode, List<AclEntry> entries, OperationUiContext context) {
        context.setParameter(StandardParameters.BEAN_FACTORY, beanFactory);
        var rootNodeWrapper = new AclMetadataElementWrapper();
        metadata.clear();
        convert(rootNode, rootNodeWrapper);
        rootNodeWrapper.getChildren().sort(Comparator.comparing(AclMetadataElementWrapper::getName));
        setRootEntry(rootNodeWrapper, context);
        setSelectedNodeId(AclEngine.ROOT_NODE_ID, context);
        aclEntries.clear();
        entries.forEach(entry -> {
            aclEntries.put(entry.getId(), entry);
        });
        updateHasRules(context);
        var rootEntry = aclEntries.get(rootNode.getId());
        entryEditor.setData(rootNode, rootEntry == null? List.of(): rootEntry.getRules(), context);
    }

    private void updateHasRules(OperationUiContext context) {
        var hasRulesIds = new ArrayList<String>();
        var hasChildrenWithRulesIds = new ArrayList<String>();
        collectRulesIds(hasRulesIds, hasChildrenWithRulesIds, metadata.get(AclEngine.ROOT_NODE_ID));
        setHasRulesIds(hasRulesIds, context);
        setHasChildrenWithRulesIds(hasChildrenWithRulesIds, context);
    }

    private boolean collectRulesIds(ArrayList<String> hasRulesIds, ArrayList<String> hasChildrenWithRulesIds, AclMetadataElement rootNode) {
        boolean result = false;
        if(aclEntries.containsKey(rootNode.getId())){
            hasRulesIds.add(rootNode.getId());
            result = true;
        }
        for(var child: rootNode.getChildren()){
            if(collectRulesIds(hasRulesIds, hasChildrenWithRulesIds, child)){
                hasChildrenWithRulesIds.add(child.getId());
                result = true;
            }
        }
        return result;

    }

    private void convert(AclMetadataElement rootNode, AclMetadataElementWrapper rootNodeWrapper) {
        rootNodeWrapper.setId(rootNode.getId());
        metadata.put(rootNode.getId(), rootNode);
        rootNodeWrapper.setName(rootNode.getName().toString(LocaleUtils.getCurrentLocale()));
        rootNode.getChildren().forEach(child -> {
            var childWrapper = new AclMetadataElementWrapper();
            convert(child, childWrapper);
            rootNodeWrapper.getChildren().add(childWrapper);
        });
    }

    public List<AclEntry> getData() {
        var result = new HashMap<String, AclEntry>(aclEntries);
        var entry = entryEditor.getData();
        if(entry == null){
            result.remove(getSelectedNodeId());
        } else {
            result.put(getSelectedNodeId(), entry);
        }
        return new ArrayList<>(result.values());
    }
}