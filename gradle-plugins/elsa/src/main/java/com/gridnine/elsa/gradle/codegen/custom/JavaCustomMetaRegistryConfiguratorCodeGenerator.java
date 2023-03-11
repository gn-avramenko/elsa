/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.custom;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.Set;

public class JavaCustomMetaRegistryConfiguratorCodeGenerator {
    public  void generate(CustomMetaRegistry registry, SerializableMetaRegistry sRegistry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.meta.custom.CustomMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.serialization.SerializableMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.config.Environment");

        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", () ->{
                gen.printLine("var smr = Environment.getPublished(SerializableMetaRegistry.class);");
                gen.printLine("var cmr = Environment.getPublished(CustomMetaRegistry.class);");
                for(String enumId: registry.getEnumsIds()){
                    var ed = sRegistry.getEnums().get(enumId);
                    JavaCodeGeneratorUtils.generateEnumMetaRegistryConfiguratorCode(ed, "smr", "cmr", gen);
                }
                for(String entityId: registry.getEntitiesIds()){
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "cmr.getEntitiesIds()", gen);
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
        generatedFiles.add(file);
    }

}
