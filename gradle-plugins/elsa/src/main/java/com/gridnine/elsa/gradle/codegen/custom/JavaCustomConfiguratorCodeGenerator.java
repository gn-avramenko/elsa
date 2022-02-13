/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.custom;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestDescription;
import com.gridnine.elsa.common.meta.rest.RestGroupDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestOperationDescription;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;

import java.io.File;
import java.util.Set;

public class JavaCustomConfiguratorCodeGenerator {
    public static void generate(CustomMetaRegistry registry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(CodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.common.meta.custom.CustomMetaRegistryConfigurator");
        gen.addImport("org.springframework.stereotype.Component");
        gen.addImport("com.gridnine.elsa.common.meta.custom.CustomMetaRegistry");
        gen.printLine("@Component");
        gen.wrapWithBlock("public class %s implements CustomMetaRegistryConfigurator".formatted(CodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.printLine("@Override");
            gen.wrapWithBlock("public void updateMetaRegistry(CustomMetaRegistry registry)", () ->{
                for(EnumDescription ed : registry.getEnums().values()){
                    CodeGeneratorUtils.generateJavaEnumConfiguratorCode(ed, gen);
                }
                for(EntityDescription ed : registry.getEntities().values()){
                    CodeGeneratorUtils.generateJavaEntityConfiguratorCode(ed, gen);
                }
            });

        });
        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
        generatedFiles.add(file);
    }

}
