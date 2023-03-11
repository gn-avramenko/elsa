/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;

import java.io.File;
import java.util.Set;

public class DomainTypesConfiguratorCodeGen {
    public void generate(DomainTypesRegistry registry, File destDir, String className, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(className);
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.elsa.meta.domain.DomainTypesRegistry");
        gen.addImport("com.gridnine.elsa.meta.config.Environment");
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
                for(DatabaseTagDescription tag: registry.getDatabaseTags().values()){
                    generateTagDescription(gen, tag, "registry.getDatabaseTags()");
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + ".java", destDir);
        generatedFiles.add(file);
    }

    private static void generateTagDescription(JavaCodeGenerator gen, DatabaseTagDescription tag, String mapName) throws Exception {
        gen.wrapWithBlock("", ()->{
            gen.addImport("com.gridnine.elsa.meta.common.DatabaseTagDescription");
            gen.printLine("var tag = new DatabaseTagDescription();");
            gen.printLine("tag.setTagName(\"%s\");".formatted(tag.getTagName()));
            gen.printLine("tag.setType(\"%s\");".formatted(tag.getType()));
            if(tag.getObjectIdAttributeName() != null){
                gen.printLine("tag.setObjectIdAttributeName(\"%s\");".formatted(tag.getObjectIdAttributeName()));
            }
            gen.printLine("tag.setType(\"%s\");".formatted(tag.getType()));
            for(AttributeDescription attr: tag.getAttributes().values()){
                JavaCodeGeneratorUtils.generateAttributeDescription(gen, attr, "tag.getAttributes()");
            }
            if(tag.isHasCollectionSupport()){
                gen.printLine("tag.setHasCollectionSupport(true);");
            }
            if(tag.isHasComparisonSupport()){
                gen.printLine("tag.setHasComparisonSupport(true);");
            }
            if(tag.isHasEqualitySupport()){
                gen.printLine("tag.setHasEqualitySupport(true);");
            }
            if(tag.isHasSortSupport()){
                gen.printLine("tag.setHasSortSupport(true);");
            }
            if(tag.isHasNumberOperationsSupport()){
                gen.printLine("tag.setHasNumberOperationsSupport(true);");
            }
            if(tag.isHasStringOperationsSupport()){
                gen.printLine("tag.setHasStringOperationsSupport(true);");
            }
            if(tag.getSearchQueryArgumentType() != null){
                gen.printLine("tag.setSearchQueryArgumentType(\"%s\");".formatted(tag.getSearchQueryArgumentType()));
            }
            if(!tag.getGenerics().isEmpty()){
                JavaCodeGeneratorUtils.processGenerics(tag.getGenerics(), 0, gen);
                gen.printLine("tag.getGenerics().addAll(generics_0);");
            }
            gen.printLine("%s.put(\"%s\", tag);".formatted(mapName, tag.getTagName()));
        });
    }
}
