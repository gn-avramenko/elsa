/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;

import java.io.File;
import java.util.Set;

public class L10nTypesConfiguratorCodeGen {
    public void generate(L10nTypesRegistry registry, File destDir, String className, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(className);
        gen.setPackageName(packageName);
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.elsa.meta.l10n.L10nTypesRegistry");
        gen.addImport("com.gridnine.elsa.core.config.Environment");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(className)), ()->{
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", ()->{
                gen.printLine("var registry = Environment.getPublished(L10nTypesRegistry.class);");
                for(TagDescription tag: registry.getParameterTypeTags().values()){
                    JavaCodeGeneratorUtils.generateTagDescription(gen, tag, "registry.getParameterTypeTags()");
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + ".java", destDir);
        generatedFiles.add(file);
    }
}
