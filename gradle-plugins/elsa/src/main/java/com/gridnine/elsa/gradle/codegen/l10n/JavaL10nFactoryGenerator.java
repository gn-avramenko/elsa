/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.l10n;

import com.gridnine.elsa.gradle.codegen.common.GenPropertyDescription;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Set;

public class JavaL10nFactoryGenerator {

    public void generate(String factory, L10nMetaRegistry l10nMetaRegistry, SerializableTypesRegistry stRegistry, L10nTypesRegistry l10Registry, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(factory));
        gen.addImport("com.gridnine.elsa.core.l10n.Localizer");
        gen.addImport("com.gridnine.elsa.core.model.common.L10nMessage");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(factory)), () -> {
            for(L10nMessagesBundleDescription bundle: l10nMetaRegistry.getBundles().values()) {
                for (L10nMessageDescription message : bundle.getMessages().values()) {
                    gen.blankLine();
                    var parameters = new StringBuilder();
                    var arguments = new StringBuilder();
                    for (PropertyDescription param : message.getParameters().values()) {
                        if (parameters.length() > 0) {
                            parameters.append(", ");
                            arguments.append(", ");
                        }
                        var gpd = new GenPropertyDescription();
                        gpd.setTagName(param.getTagName());
                        gpd.setId(param.getId());
                        var tag = l10Registry.getParameterTypeTags().get(param.getTagName());
                        gpd.setTagDescription(tag);
                        var type = JavaCodeGeneratorUtils.calculateDeclarationType(gpd, stRegistry, gen);
                        parameters.append("%s %s".formatted(type, param.getId()));
                        arguments.append(param.getId());
                    }
                    gen.wrapWithBlock("public String %s(%s)".formatted(message.getId(), parameters), () -> {
                        if (arguments.length() > 0) {
                            gen.printLine("return Localizer.get().toString(\"%s\", \"%s\", null, %s);".formatted(bundle.getId(), message.getId(), arguments));
                        } else {
                            gen.printLine("return Localizer.get().toString(\"%s\", \"%s\", null);".formatted(bundle.getId(), message.getId()));
                        }
                    });
                    gen.blankLine();
                    gen.wrapWithBlock("public static L10nMessage %sMessage(%s)".formatted(message.getId(), parameters), () -> {
                        if (arguments.length() > 0) {
                            gen.printLine("return new L10nMessage(\"%s\", \"%s\", %s);".formatted(bundle.getId(), message.getId(), arguments));
                        } else {
                            gen.printLine("return new L10nMessage(\"%s\", \"%s\");".formatted(bundle.getId(), message.getId()));
                        }
                    });
                }
            }
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), factory+".java", destDir);
        generatedFiles.add(file);
    }
}
