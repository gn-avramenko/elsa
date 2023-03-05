/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;

import java.io.File;
import java.util.Set;

public class DomainTypesConfiguratorCodeGen {
    public void generate(DomainTypesRegistry registry, File destDir, String className, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(className);
        gen.setPackageName(packageName);
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.elsa.meta.domain.DomainTypesRegistry");
        gen.addImport("com.gridnine.elsa.core.config.Environment");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(className)), ()->{
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", ()->{
                gen.printLine("var registry = Environment.getPublished(DomainTypesRegistry.class);");
                for(AttributeDescription attr: registry.getAssetAttributes().values()){
                    JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "registry.getAssetAttributes()");
                }
                for(AttributeDescription attr: registry.getDocumentAttributes().values()){
                    JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "registry.getDocumentAttributes()");
                }
                for(AttributeDescription attr: registry.getProjectionAttributes().values()){
                    JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "registry.getProjectionAttributes()");
                }
                for(AttributeDescription attr: registry.getEnumAttributes().values()){
                    JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "registry.getEnumAttributes()");
                }
                for(AttributeDescription attr: registry.getEnumItemAttributes().values()){
                    JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "registry.getEnumItemAttributes()");
                }
                for(TagDescription tag: registry.getEntityTags().values()){
                    JavaCodeGeneratorUtils.generateTagDescription(gen, tag, "registry.getEntityTags()");
                }
                for(TagDescription tag: registry.getDatabaseTags().values()){
                    JavaCodeGeneratorUtils.generateTagDescription(gen, tag, "registry.getDatabaseTags()");
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + ".java", destDir);
        generatedFiles.add(file);
    }
}
