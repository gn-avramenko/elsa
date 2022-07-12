/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui;

import com.gridnine.elsa.common.meta.common.XmlNode;
import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.common.meta.ui.UiViewDescription;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.WebCodeGeneratorUtils;
import com.gridnine.elsa.gradle.parser.ui.UiMetaRegistryParser;
import com.gridnine.elsa.gradle.parser.ui.ViewTemplateParserHandler;
import com.gridnine.elsa.gradle.plugin.ElsaWebExtension;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import org.gradle.api.Project;

import java.io.File;
import java.util.*;

public class WebUiCodeGenerator implements CodeGenerator<WebUiCodeGenRecord> {
    @Override
    public void generate(List<WebUiCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var tsAssociations = (Map<String, File>) context.get("ts-associations");
        if(tsAssociations == null){
            tsAssociations = new LinkedHashMap<>();
            context.put("ts-associations", tsAssociations);
        }
        var tsa = tsAssociations;
        var fullTemplateRegistry = (UiMetaRegistry) context.get("full-ui-meta-registry");
        if(fullTemplateRegistry == null){
            fullTemplateRegistry = new UiMetaRegistry();
        }
        var ftr = fullTemplateRegistry;
        Project project = (Project) context.get("project");
        var ext = project.getExtensions().getByType(ElsaWebExtension.class);
        tsa.putAll(ext.getImports());
        var handlers = ext.getTemplatesHandlers();
        var parser = new UiMetaRegistryParser();
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var metaRegistry = new UiMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources(),context);
                parser.updateMetaRegistry(ftr, it.getSources(), context);
                var additionalEntities = new LinkedHashSet<String>();
                for(var view: metaRegistry.getViews().values()){
                    updateImportEntities(view, additionalEntities, handlers, ftr);
                }
                var gen = new TypeScriptCodeGenerator();
                gen.printLine("/* eslint-disable max-classes-per-file,no-unused-vars,max-len,lines-between-class-members  */");
                WebCodeGeneratorUtils.generateImportCode(metaRegistry.getEntities().values(), additionalEntities, tsa, gen, new File(destDir, it.getTsFileName()));
                for(var en: metaRegistry.getEnums().values()){
                    tsa.put(JavaCodeGeneratorUtils.getSimpleName(en.getId()), new File(destDir, it.getTsFileName()));
                    WebCodeGeneratorUtils.generateWebEnumCode(en, gen);
                }
                for(var ett: metaRegistry.getEntities().values()){
                    tsa.put(JavaCodeGeneratorUtils.getSimpleName(ett.getId()), new File(destDir, it.getTsFileName()));
                    WebCodeGeneratorUtils.generateWebEntityCode(ett,  gen);
                }
                for(var view: metaRegistry.getViews().values()){
                    generateViewCode(view, gen, handlers, ftr, tsa, new File(destDir, it.getTsFileName()));
                }
                var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), it.getTsFileName(), destDir);
                generatedFiles.add(file);
            });
        });
    }

    private void updateImportEntities(UiViewDescription view, Set<String> additionalEntities, Map<String, ViewTemplateParserHandler> handlers, UiMetaRegistry ftr) {
        var handler = handlers.get(view.getView().getName());
        if(handler == null){
            return;
        }
        handler.updateImports(additionalEntities, view.getView(), ftr, handlers);
    }

    private void generateViewCode(UiViewDescription view, TypeScriptCodeGenerator gen, Map<String, ViewTemplateParserHandler> handlers, UiMetaRegistry fullTemplateRegistry, Map<String, File> tsa, File file ) throws Exception {
        var viewNode = view.getView();
        var handler = handlers.get(viewNode.getName());
        if(handler == null){
            return;
        }
        var viewNodes = handler.getAllViewNodes(view.getView(), handlers);
        for(XmlNode viewNode2: viewNodes){
            var handler2 = handlers.get(viewNode2.getName());
            tsa.put(JavaCodeGeneratorUtils.getSimpleName(handler2.getId(viewNode2)), file);
            gen.wrapWithBlock("export class %s extends %s ".formatted(JavaCodeGeneratorUtils.getSimpleName(handler2.getId(viewNode2)), handler2.getWidgetClassName(viewNode2)), () ->{
                var members = handler2.getViewMembers(viewNode2, handlers, fullTemplateRegistry);
                for(var member: members){
                    gen.printLine("// @ts-ignore");
                    gen.printLine("%s: %s;".formatted(member.getId(), member.getWidgetClass()));
                }
            });
            gen.blankLine();
        }

    }
}
