/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui.template;

import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.WebCodeGeneratorUtils;
import com.gridnine.elsa.gradle.parser.ui.template.UiTemplateMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class WebUiTemplateCodeGenerator implements CodeGenerator<WebUiTemplateCodeGenRecord> {
    @Override
    public void generate(List<WebUiTemplateCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var parser = new UiTemplateMetaRegistryParser();
        var fullTemplateRegistry = (UiMetaRegistry) context.get("full-ui-meta-registry");
        if(fullTemplateRegistry == null){
            fullTemplateRegistry = new UiMetaRegistry();
            context.put("full-ui-meta-registry", fullTemplateRegistry);
        }
        var ftr = fullTemplateRegistry;
        var tsAssociations = (Map<String, File>) context.get("ts-associations");
        if(tsAssociations == null){
            tsAssociations = new LinkedHashMap<>();
            context.put("ts-associations", tsAssociations);
        }
        var tsa = tsAssociations;
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var metaRegistry = new UiMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                parser.updateMetaRegistry(ftr, it.getSources());
                var gen = new TypeScriptCodeGenerator();
                WebCodeGeneratorUtils.generateImportCode(metaRegistry.getEntities().values(), new HashSet<>(), tsa, gen, new File(destDir, it.getTsFileName()));
                for(var en: metaRegistry.getEnums().values()){
                    WebCodeGeneratorUtils.generateWebEnumCode(en, gen);
                }
                for(var ett: metaRegistry.getEntities().values()){
                    WebCodeGeneratorUtils.generateWebEntityCode(ett, gen);
                }
                var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), it.getTsFileName(), destDir);
                metaRegistry.getEnums().keySet().forEach(it2 -> tsa.put(it2, file));
                metaRegistry.getEntities().keySet().forEach(it2 -> tsa.put(it2, file));
                generatedFiles.add(file);
            });
        });
    }
}
