/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableType;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;

import java.io.File;
import java.util.List;
import java.util.Set;

public class SerializableTypesConfiguratorCodeGen {
    public void generate(SerializableTypesRegistry registry, File destDir, String className, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(className);
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.elsa.meta.serialization.SerializableTypesRegistry");
        gen.addImport("com.gridnine.elsa.core.config.Environment");
        gen.addImport("com.gridnine.elsa.meta.serialization.SerializableType");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(className)), ()->{
            for(SerializableType type: registry.getTypes().values()){
                gen.blankLine();
                gen.printLine("public static final String %s = \"%s\";".formatted(type.getId().replaceAll("-","_"), type.getId()));
            }
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", ()->{
                gen.printLine("var registry = Environment.getPublished(SerializableTypesRegistry.class);");
                  for(SerializableType type: registry.getTypes().values()){
                   gen.blankLine();
                   gen.wrapWithBlock("",()->{
                       gen.printLine("var type = new SerializableType();");
                       gen.printLine("var id = \"%s\";".formatted(type.getId()));
                       gen.printLine("type.setId(id);");
                       gen.printLine("type.setJavaQualifiedName(\"%s\");".formatted(type.getJavaQualifiedName()));
                       if(!type.getGenerics().isEmpty()){
                           processGenerics(type.getGenerics(), 0, gen);
                           gen.printLine("type.getGenerics().addAll(genererics_0);");
                       }
                       gen.printLine("registry.getTypes().put(id, type);");
                   });
                }
            });
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + ".java", destDir);
        generatedFiles.add(file);
    }

    private void processGenerics(List<GenericDeclaration> generics, int i, JavaCodeGenerator gen) throws Exception {

        gen.addImport("com.gridnine.elsa.meta.serialization.GenericDeclaration");
        gen.addImport("java.util.ArrayList");
        gen.printLine("var genererics_%s = new ArrayList<GenericDeclaration>();".formatted(i));
        for(GenericDeclaration generic: generics){
            if(generic instanceof SingleGenericDeclaration sg){
                gen.addImport("com.gridnine.elsa.meta.serialization.SingleGenericDeclaration");
                gen.wrapWithBlock("",()->{
                    gen.printLine("var generic = new SingleGenericDeclaration();");
                    gen.printLine("generic.setId(\"%s\");".formatted(sg.getId()));
                    gen.printLine("genererics_%s.add(generic);".formatted(i));
                });
                continue;
            }
            GenericsDeclaration generics2 = (GenericsDeclaration) generic;
            processGenerics(generics2.getGenerics(), i+1, gen);
            gen.printLine("genererics_%s.add(genererics_%s);".formatted(i, i+1));
        }
    }
}
