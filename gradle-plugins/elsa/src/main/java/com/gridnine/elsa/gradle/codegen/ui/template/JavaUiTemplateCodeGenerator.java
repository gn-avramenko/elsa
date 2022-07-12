/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui.template;

import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.ui.JavaUiEntitiesGenerator;
import com.gridnine.elsa.gradle.parser.ui.template.UiTemplateMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUiTemplateCodeGenerator implements CodeGenerator<JavaUiTemplateCodeGenRecord> {
    @Override
    public void generate(List<JavaUiTemplateCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var fullTemplateRegistry = (UiMetaRegistry) context.get("full-ui-meta-registry");
        if(fullTemplateRegistry == null){
            fullTemplateRegistry = new UiMetaRegistry();
            context.put("full-ui-meta-registry", fullTemplateRegistry);
        }
        var configurators = new LinkedHashMap<String, List<File>>();
        var parser = new UiTemplateMetaRegistryParser();
        var ftr = fullTemplateRegistry;
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var metaRegistry = new UiMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                parser.updateMetaRegistry(ftr, it.getSources());
                BuildExceptionUtils.wrapException(() -> JavaUiTemplateXsdGenerator.generate(metaRegistry, destDir, it.getXsdFileName(), it.getTargetNameSpace(), generatedFiles));
                BuildExceptionUtils.wrapException(() -> JavaUiEntitiesGenerator.generate(metaRegistry, destDir,  generatedFiles));
            });
        });
    }
}
