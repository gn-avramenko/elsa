/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.XsdCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.XsdGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;

public class L10nXsdCodeGen {
    public void generate(L10nTypesRegistry registry, SerializableTypesRegistry sRegistry, File xsdDir, String customizationSuffix) throws Exception {
        var gen = new XsdCodeGenerator();
        gen.nameSpace("http://gridnine.com/elsa/meta-l10n" + (customizationSuffix != null ? "-%s".formatted(customizationSuffix) : ""));
        gen.wrapWithBlock("element", gen.attributes("name", "messages-bundle"), () -> {
            gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                    gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                        gen.wrapWithBlock("sequence", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                            gen.wrapWithBlock("sequence", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                                gen.wrapWithBlock("element", gen.attributes("name", "message"), () -> {
                                    gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                                        gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                            gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                                                gen.wrapWithBlock("sequence", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                                                    gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                                                        for (TagDescription tag : registry.getParameterTypeTags().values()) {
                                                            gen.addTag("element", "name", tag.getTagName(), "type", "tns:paramter-tag-%s".formatted(tag.getTagName()));
                                                        }
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
        gen.wrapWithBlock("complexType", gen.attributes("name", "ElementWithId"), () -> {
            gen.addTag("attribute", "use", "required", "name", "id", "type", "string");
        });
        XsdGeneratorUtils.processTags(registry.getParameterTypeTags().values(), "parameter", gen);
        JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "l10n.xsd", xsdDir);
    }

}
