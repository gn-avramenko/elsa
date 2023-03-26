/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableType;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Set;

public class JavaDomainFieldsClassCodeGenerator {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry str, DomainTypesRegistry dtr, File destDir, Set<File> generatedFiles) throws Exception {
        registry.getProjectionsIds().forEach(it -> BuildExceptionUtils.wrapException(() -> generateFieldsClass(sRegistry.getEntities().get(it), dtr, str, destDir, generatedFiles)));
        registry.getAssetsIds().forEach(it -> BuildExceptionUtils.wrapException(() -> generateFieldsClass(sRegistry.getEntities().get(it), dtr, str, destDir, generatedFiles)));
    }

    private void generateFieldsClass(EntityDescription sd, DomainTypesRegistry dtr, SerializableTypesRegistry totalStRegistry, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(sd.getId());
        gen.setPackageName(packageName);
        gen.wrapWithBlock("public class %sFields".formatted(JavaCodeGeneratorUtils.getSimpleName(sd.getId())), () -> {
            for(PropertyDescription prop: sd.getProperties().values()){
                gen.blankLine();
                gen.printLine("public final static _%1$sField %1$s = new _%1$sField();".formatted(prop.getId()));
            }
            for(PropertyDescription prop: sd.getProperties().values()){
                gen.blankLine();
                gen.addImport("com.gridnine.elsa.common.search.FieldNameSupport");
                var sb = new StringBuilder("private static class _%sField extends FieldNameSupport".formatted(prop.getId()));
                DatabaseTagDescription dtd = dtr.getDatabaseTags().get(prop.getTagName());
                if(dtd.isHasStringOperationsSupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.StringOperationsSupport", gen);
                }
                if(dtd.isHasSortSupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.SortSupport", gen);
                }
                if(dtd.isHasEqualitySupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.EqualitySupport", gen);
                }
                if(dtd.isHasComparisonSupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.ComparisonSupport", gen);
                }
                if(dtd.isHasNumberOperationsSupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.NumberOperationsSupport", gen);
                }
                if(dtd.isHasCollectionSupport()){
                    addImplements(sb, "com.gridnine.elsa.common.search.CollectionSupport", gen);
                }
                if(dtd.getSearchQueryArgumentType() != null){
                    gen.addImport("com.gridnine.elsa.common.search.ArgumentType");
                    if(dtd.getSearchQueryArgumentType().equals("ENUM")){
                        //TODO dont hardcode
                        String className = prop.getAttributes().get("class-name");
                        if(className == null){
                            className = prop.getAttributes().get("element-class-name");
                        }
                        sb.append(", ArgumentType<%s>".formatted(JavaCodeGeneratorUtils.getSimpleName(className, gen)));
                    } else {
                        SerializableType st = totalStRegistry.getTypes().get(dtd.getSearchQueryArgumentType());
                        //TODO dont hardcode
                        if ("ENTITY-REFERENCE".equals(st.getId())) {
                            gen.addImport(st.getJavaQualifiedName());
                            String className = prop.getAttributes().get("class-name");
                            if(className == null){
                                className = prop.getAttributes().get("element-class-name");
                            }
                            sb.append(", ArgumentType<EntityReference<%s>>".formatted(JavaCodeGeneratorUtils.getSimpleName(className)));
                        } else {
                            var type = JavaCodeGeneratorUtils.getSimpleName(st.getJavaQualifiedName(), gen);

                            sb.append(", ArgumentType<%s>".formatted(type));
                        }
                    }
                }
                gen.wrapWithBlock(sb.toString(), () -> gen.wrapWithBlock("_%sField()".formatted(prop.getId()), () -> gen.printLine("super(\"%s\");".formatted(prop.getId()))));
            }
        });

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "%sFields.java"
                .formatted(sd.getId()), destDir);
        generatedFiles.add(file);
    }

    private static void addImplements(StringBuilder sb, String name, JavaCodeGenerator gen) {
        if(sb.indexOf("implements") == -1){
            sb.append(" implements ");
        } else {
            sb.append(", ");
        }
        sb.append(JavaCodeGeneratorUtils.getSimpleName(name, gen));
    }
}
