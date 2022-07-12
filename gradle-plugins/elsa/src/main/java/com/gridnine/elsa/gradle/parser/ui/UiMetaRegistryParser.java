/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.ui;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.XmlNode;
import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.common.meta.ui.UiViewDescription;
import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.plugin.ElsaJavaExtension;
import com.gridnine.elsa.gradle.plugin.ElsaWebExtension;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import org.gradle.api.Project;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UiMetaRegistryParser {

    public void updateMetaRegistry(UiMetaRegistry registry,  List<File> sources, Map<Object,Object> context) {
        var fullTemplateRegistry = (UiMetaRegistry) context.get("full-ui-meta-registry");
        if(fullTemplateRegistry == null){
            fullTemplateRegistry = new UiMetaRegistry();
            context.put("full-ui-meta-registry", fullTemplateRegistry);
        }
        var fullRegistry = fullTemplateRegistry;
        Project project = (Project) context.get("project");
        var javaExt = project.getExtensions().findByType(ElsaJavaExtension.class);
        Map<String, ViewTemplateParserHandler> handlers;
        if(javaExt != null){
            handlers = javaExt.getTemplatesHandlers();
        } else {
            handlers = project.getExtensions().findByType(ElsaWebExtension.class).getTemplatesHandlers();
        }
        var callback = new ViewTemplateParserHandlerCallback() {
            @Override
            public void addEntity(EntityDescription ed) {
                registry.getEntities().put(ed.getId(), ed);
            }
            @Override
            public void addViewDescription(String id, XmlNode view, Map<String, Map<Locale, String>> localizations) {
                var vd = new UiViewDescription(id);
                vd.setView(view);
                vd.getLocalizations().putAll(localizations);
                registry.getViews().put(id, vd);
                fullRegistry.getViews().put(id, vd);
            }

            @Override
            public ViewTemplateParserHandler getHandler(String tagName) {
                return handlers.get(tagName);
            }

            @Override
            public UiMetaRegistry getFullRegistry() {
                return fullRegistry;
            }
        };
        sources.forEach(it -> BuildExceptionUtils.wrapException(() -> {
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            XmlNode node = pr.node();
            for(XmlNode child: node.getChildren()){
                if("enum".equals(child.getName())){
                    CommonParserUtils.updateEnum(registry.getEnums(), child, pr.localizations());
                } else {
                    var handler = handlers.get(child.getName());
                    if(handler != null){
                        handler.addEntities(child, pr.localizations(), callback);
                    }
                }
            }
        }));
    }

}
