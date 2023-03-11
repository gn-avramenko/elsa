/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.Set;

public class JavaDomainMetaRegistryConfiguratorCodeGen {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, String configurator,File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.meta.domain.DomainMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.serialization.SerializableMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.config.Environment");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", () ->{
                gen.printLine("var smr = Environment.getPublished(SerializableMetaRegistry.class);");
                gen.printLine("var dmr = Environment.getPublished(DomainMetaRegistry.class);");
                for(String enumId: registry.getEnumsIds()){
                    var ed = sRegistry.getEnums().get(enumId);
                    JavaCodeGeneratorUtils.generateEnumMetaRegistryConfiguratorCode(ed, "smr", "dmr", gen);
                }
                for(String entityId: registry.getEntitiesIds()){
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "dmr.getEntitiesIds()", gen);
                }
                for(String entityId: registry.getAssetsIds()){
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "dmr.getAssetsIds()", gen);
                }
                for(String entityId: registry.getDocumentsIds()){
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "dmr.getDocumentsIds()", gen);
                }
                for(String entityId: registry.getProjectionsIds()){
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "dmr.getProjectionsIds()", gen);
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
        generatedFiles.add(file);
    }
}
