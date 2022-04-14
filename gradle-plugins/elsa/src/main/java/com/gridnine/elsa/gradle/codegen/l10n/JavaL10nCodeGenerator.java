/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.l10n;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;
import com.gridnine.elsa.common.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessageParameterDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.parser.l10n.L10nMetaRegistryParser;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class JavaL10nCodeGenerator  implements CodeGenerator<JavaL10nCodeGenRecord> {
    @Override
    public void generate(List<JavaL10nCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var parser = new L10nMetaRegistryParser();
        for(JavaL10nCodeGenRecord record: records){
            var reg = new L10nMetaRegistry();
            parser.updateMetaRegistry(reg, record.getSource());
            if(!reg.getBundles().isEmpty()){
                var bundle = reg.getBundles().values().iterator().next();
                generateConfigurator(record.getRegistryConfigurator(), bundle, destDir, generatedFiles);
                generateFactory(record.getFactory(), bundle, destDir, generatedFiles);
            }
        }
    }

    private void generateConfigurator(String configurator, L10nMessagesBundleDescription bundle, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(CodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMetaRegistryConfigurator");
        gen.addImport("org.springframework.stereotype.Component");
        gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry");
        gen.printLine("@Component");
        gen.wrapWithBlock("public class %s implements L10nMetaRegistryConfigurator".formatted(CodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.printLine("@Override");
            gen.wrapWithBlock("public void updateMetaRegistry(L10nMetaRegistry registry)", () ->{
                gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMessagesBundleDescription");
                gen.wrapWithBlock(null, () -> {
                    gen.printLine("var bundleDescription = new L10nMessagesBundleDescription(\"%s\");".formatted(bundle.getId()));
                    gen.printLine("registry.getBundles().put(bundleDescription.getId(), bundleDescription);");
                    for (L10nMessageDescription md : bundle.getMessages().values()) {
                        gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMessageDescription");
                        gen.wrapWithBlock(null, () -> {
                            gen.printLine("var messageDescription = new L10nMessageDescription(\"%s\");".formatted(md.getId()));
                            for(Map.Entry<String, L10nMessageParameterDescription> entry: md.getParameters().entrySet()){
                                gen.wrapWithBlock(null, () -> {
                                    gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMessageParameterDescription");
                                    gen.addImport("com.gridnine.elsa.common.meta.common.StandardValueType");
                                    gen.printLine("var paramDescription = new L10nMessageParameterDescription(\"%s\");".formatted(entry.getKey()));
                                    var param = entry.getValue();
                                    gen.printLine("paramDescription.setType(StandardValueType.%s);".formatted(param.getType().name()));
                                    if(param.isCollection()){
                                        gen.printLine("paramDescription.setCollection(true);");
                                    }
                                    if(param.getClassName() != null){
                                        gen.printLine("paramDescription.setClassName(\"%s\");".formatted(param.getClassName()));
                                    }
                                    gen.printLine("messageDescription.getParameters().put(paramDescription.getId(), paramDescription);");
                                });
                            }
                            for (Map.Entry<Locale, String> entry : md.getDisplayNames().entrySet()) {
                                gen.addImport("com.gridnine.elsa.common.core.utils.LocaleUtils");
                                gen.printLine("messageDescription.getDisplayNames().put(LocaleUtils.getLocale(\"%s\",\"%s\"), \"%s\");".formatted(entry.getKey().getLanguage(), entry.getKey().getCountry(), entry.getValue()));
                            }
                            gen.printLine("bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);");
                        });
                    }
                });
            });

        });
        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
        generatedFiles.add(file);
    }

    private void generateFactory(String factory, L10nMessagesBundleDescription bundle, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(CodeGeneratorUtils.getPackage(factory));
        gen.addImport("com.gridnine.elsa.common.core.l10n.Localizer");
        gen.addImport("com.gridnine.elsa.common.core.model.common.L10nMessage");
        gen.addImport("org.springframework.stereotype.Component");
        gen.addImport("org.springframework.beans.factory.annotation.Autowired");
        gen.printLine("@Component");
        gen.wrapWithBlock("public class %s".formatted(CodeGeneratorUtils.getSimpleName(factory)), () -> {
            gen.blankLine();
            gen.printLine("@Autowired");
            gen.printLine("private Localizer localizer;");

            for(L10nMessageDescription message: bundle.getMessages().values()){
               gen.blankLine();
                var parameters = new StringBuilder();
                var arguments = new StringBuilder();
                for(L10nMessageParameterDescription param: message.getParameters().values()){
                    if(parameters.length() > 0){
                        parameters.append(", ");
                        arguments.append(", ");
                    }
                    if(param.isCollection()){
                        gen.addImport("java.util.List");
                        parameters.append("List<%s> %s".formatted(CodeGeneratorUtils.getPropertyType(param.getType(), param.getClassName(), true, gen), param.getId()));
                    } else {
                        parameters.append("%s %s".formatted(CodeGeneratorUtils.getPropertyType(param.getType(), param.getClassName(), true, gen), param.getId()));
                    }
                    arguments.append(param.getId());
                }
                gen.wrapWithBlock("public String %s(%s)".formatted(message.getId(), parameters), () ->{
                    if(arguments.length() > 0) {
                        gen.printLine("return localizer.toString(\"%s\", \"%s\", null, %s);".formatted(bundle.getId(), message.getId(), arguments));
                    } else {
                        gen.printLine("return localizer.toString(\"%s\", \"%s\", null);".formatted(bundle.getId(), message.getId()));
                    }
                });
                gen.blankLine();
                gen.wrapWithBlock("public static L10nMessage %sMessage(%s)".formatted(message.getId(), parameters), () ->{
                    if(arguments.length() > 0) {
                        gen.printLine("return new L10nMessage(\"%s\", \"%s\", %s);".formatted(bundle.getId(), message.getId(), arguments));
                    } else {
                        gen.printLine("return new L10nMessage(\"%s\", \"%s\");".formatted(bundle.getId(), message.getId()));
                    }
                });
            }
        });
        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), factory+".java", destDir);
        generatedFiles.add(file);
    }
}
