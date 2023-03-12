/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.l10n;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class JavaL10nMetaRegistryConfiguratorCodeGen {
    public void generate(L10nMetaRegistry registry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        for(L10nMessagesBundleDescription bundle: registry.getBundles().values()){
            var gen = new JavaCodeGenerator();
            gen.setPackageName(JavaCodeGeneratorUtils.getPackage(configurator));
            gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry");
            gen.addImport("com.gridnine.elsa.meta.config.Environment");
            gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(configurator)), () -> {
                gen.blankLine();
                gen.wrapWithBlock("public void configure()", () ->{
                    gen.printLine("var registry = Environment.getPublished(L10nMetaRegistry.class);");
                    gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMessagesBundleDescription");
                    gen.wrapWithBlock(null, () -> {
                        gen.printLine("var bundleDescription = new L10nMessagesBundleDescription(\"%s\");".formatted(bundle.getId()));
                        gen.printLine("registry.getBundles().put(bundleDescription.getId(), bundleDescription);");
                        for (L10nMessageDescription md : bundle.getMessages().values()) {
                            gen.addImport("com.gridnine.elsa.common.meta.l10n.L10nMessageDescription");
                            gen.wrapWithBlock(null, () -> {
                                gen.printLine("var messageDescription = new L10nMessageDescription(\"%s\");".formatted(md.getId()));
                                for(Map.Entry<String, PropertyDescription> entry: md.getParameters().entrySet()){
                                    gen.wrapWithBlock("", ()->{
                                        gen.addImport("com.gridnine.elsa.meta.common.PropertyDescription");
                                        gen.printLine("var parameterDescription = new PropertyDescription(\"%s\");".formatted(entry.getValue().getId()));
                                        gen.printLine("parameterDescription.setTagName(\"%s\");".formatted(entry.getValue().getTagName()));
                                        JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(entry.getValue(), "parameterDescription", gen);
                                        gen.printLine("messageDescription.getParameters().put(\"%s\", parameterDescription);".formatted(entry.getValue().getId()));
                                    });
                                }
                                for(Map.Entry<Locale, String> entry : md.getDisplayNames().entrySet()){
                                    gen.addImport("java.util.Locale");
                                    gen.addImport("com.gridnine.elsa.core.utils.LocaleUtils");
                                    if(Locale.ROOT.equals(entry.getKey())){
                                        gen.printLine("messageDescription.getDisplayNames().put(Locale.ROOT, \"%s\");".formatted(entry.getValue()));
                                    } else if (entry.getKey().getCountry() == null || "".equals(entry.getKey().getCountry())){
                                        gen.printLine("messageDescription.getDisplayNames().put(LocaleUtils.getLocale(\"%s\"), \"%s\");".formatted(entry.getKey().getLanguage(),  entry.getValue()));
                                    } else {
                                        gen.printLine("messageDescriptions.getDisplayNames().put(LocaleUtils.getLocale(\"%s\",\"%s\"), \"%s\");".formatted(entry.getKey().getLanguage(), entry.getKey().getCountry(), entry.getValue()));
                                    }
                                }
                                gen.printLine("bundleDescription.getMessages().put(messageDescription.getId(), messageDescription);");
                            });
                        }
                    });
                });

            });
            var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator+".java", destDir);
            generatedFiles.add(file);
        }

    }
}
