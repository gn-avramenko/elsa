/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui.template;

import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
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
        var configurators = new LinkedHashMap<String, List<File>>();
        var parser = new UiTemplateMetaRegistryParser();
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var metaRegistry = new UiMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                BuildExceptionUtils.wrapException(() -> JavaUiTemplateXsdGenerator.generate(metaRegistry, destDir, it.getXsdFileName(), it.getTargetNameSpace(), generatedFiles));
            });
        });
    }
}
