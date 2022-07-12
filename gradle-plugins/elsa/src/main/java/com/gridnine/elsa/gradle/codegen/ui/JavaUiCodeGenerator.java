/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui;

import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.parser.ui.UiMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUiCodeGenerator implements CodeGenerator<JavaUiCodeGenRecord> {
    @Override
    public void generate(List<JavaUiCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var parser = new UiMetaRegistryParser();
        records.forEach(it ->{
            var reg  = new UiMetaRegistry();
            parser.updateMetaRegistry(reg, it.getSources(), context);
            BuildExceptionUtils.wrapException(() -> BuildExceptionUtils.wrapException(() -> JavaUiEntitiesGenerator.generate(reg, destDir,  generatedFiles)));
        });
    }
}
