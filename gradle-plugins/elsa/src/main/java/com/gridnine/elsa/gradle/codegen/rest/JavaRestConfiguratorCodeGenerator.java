/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.rest;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.rest.RestDescription;
import com.gridnine.elsa.common.meta.rest.RestGroupDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestOperationDescription;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;

import java.io.File;
import java.util.Set;

public class JavaRestConfiguratorCodeGenerator {
    public static void generate(RestMetaRegistry registry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(CodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.common.meta.rest.RestMetaRegistryConfigurator");
        gen.addImport("org.springframework.stereotype.Component");
        gen.addImport("com.gridnine.elsa.common.meta.rest.RestMetaRegistry");
        gen.printLine("@Component");
        gen.wrapWithBlock("public class %s implements RestMetaRegistryConfigurator".formatted(CodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.printLine("@Override");
            gen.wrapWithBlock("public void updateMetaRegistry(RestMetaRegistry registry)", () ->{
                for(EnumDescription ed : registry.getEnums().values()){
                    CodeGeneratorUtils.generateJavaEnumConfiguratorCode(ed, gen);
                }
                for(EntityDescription ed : registry.getEntities().values()){
                    CodeGeneratorUtils.generateJavaEntityConfiguratorCode(ed, gen);
                }
                for(RestGroupDescription gd: registry.getGroups().values()){
                    gen.addImport("com.gridnine.elsa.common.meta.rest.RestGroupDescription");
                    gen.wrapWithBlock(null, () ->{
                        gen.printLine("var groupDescription = new RestGroupDescription(\"%s\");".formatted(gd.getId()));
                        gen.printLine("groupDescription.setRestId(\"%s\");;".formatted(gd.getRestId()));
                        gen.printLine("registry.getGroups().put(groupDescription.getId(), groupDescription);");
                    });
                }
                for(RestDescription rd: registry.getRests().values()){
                    gen.addImport("com.gridnine.elsa.common.meta.rest.RestDescription");
                    gen.wrapWithBlock(null, () ->{
                        gen.printLine("var restDescription = new RestDescription(\"%s\");".formatted(rd.getId()));
                        gen.printLine("registry.getRests().put(restDescription.getId(), restDescription);");
                    });
                }
                for(RestOperationDescription od: registry.getOperations().values()){
                    gen.addImport("com.gridnine.elsa.common.meta.rest.RestOperationDescription");
                    gen.wrapWithBlock(null, () ->{
                        gen.printLine("var operationDescription = new RestOperationDescription(\"%s\");".formatted(od.getId()));
                        gen.printLine("operationDescription.setGroupId(\"%s\");;".formatted(od.getGroupId()));
                        gen.printLine("operationDescription.setHandler(\"%s\");;".formatted(od.getHandler()));
                        gen.printLine("operationDescription.setRequestEntity(\"%s\");;".formatted(od.getRequestEntity()));
                        gen.printLine("operationDescription.setResponseEntity(\"%s\");;".formatted(od.getResponseEntity()));
                        gen.printLine("registry.getOperations().put(operationDescription.getId(), operationDescription);");
                    });
                }
            });

        });
        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
        generatedFiles.add(file);
    }

}
