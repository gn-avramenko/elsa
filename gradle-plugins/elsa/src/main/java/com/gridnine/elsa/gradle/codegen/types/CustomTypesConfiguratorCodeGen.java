/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;

import java.io.File;
import java.util.Set;

public class CustomTypesConfiguratorCodeGen {
    public void generate(CustomTypesRegistry registry, File destDir, String className, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(className);
        gen.setPackageName(packageName);
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.elsa.meta.custom.CustomTypesRegistry");
        gen.addImport("com.gridnine.elsa.core.config.Environment");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(className)), ()->{
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", ()->{
                gen.printLine("var registry = Environment.getPublished(CustomTypesRegistry.class);");
                for(TagDescription tag: registry.getEntityTags().values()){
                    JavaCodeGeneratorUtils.generateTagDescription(gen, tag, "registry.getEntityTags()");
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + ".java", destDir);
        generatedFiles.add(file);
    }
}
