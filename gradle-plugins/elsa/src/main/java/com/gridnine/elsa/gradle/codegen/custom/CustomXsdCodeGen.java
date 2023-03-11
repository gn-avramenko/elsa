/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.custom;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.XsdCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.XsdGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Collections;

public class CustomXsdCodeGen {
    public void generate(CustomTypesRegistry registry, SerializableTypesRegistry sRegistry, File xsdDir, String customizationSuffix) throws Exception {
        var gen = new XsdCodeGenerator();
        gen.nameSpace("http://gridnine.com/elsa/meta-custom" + (customizationSuffix != null ? "-%s".formatted(customizationSuffix) : ""));
        gen.wrapWithBlock("element", gen.attributes("name", "custom"), () -> {
            gen.wrapWithBlock("complexType", gen.attributes(), () -> {

                gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                    gen.wrapWithBlock("element", gen.attributes("name", "entity"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                                            gen.addTag("element", "name", "parameter", "type", "tns:ParameterType");
                                        });
                                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                                            for (TagDescription tag : registry.getEntityTags().values()) {
                                                gen.addTag("element", "name", tag.getTagName(), "type", "tns:entity-tag-%s".formatted(tag.getTagName()));
                                            }
                                        });
                                    });
                                    gen.addTag("attribute", "name", "abstract", "type", "boolean", "default", "false");
                                    gen.addTag("attribute", "name", "extends", "type", "string");
                                    gen.addTag("attribute", "name", "displayName", "type", "string");
                                });
                            });
                        });

                    });
                    gen.wrapWithBlock("element", gen.attributes("name", "enum"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), () -> {
                                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                                        XsdGeneratorUtils.genEnumItemElement(Collections.emptyMap(), gen);
                                    });
                                    gen.addTag("attribute", "name", "displayName", "type", "string");
                                });
                            });
                        });
                    });
                });
            });
        });
        gen.wrapWithBlock("complexType", gen.attributes("name", "IdWithParametersType"), () -> {
            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                    gen.wrapWithBlock("sequence", gen.attributes("minOccurs", "0", "maxOccurs", "unbounded"), () -> {
                        gen.addTag("element", "name", "parameter", "type", "tns:ParameterType");
                    });
                });
            });
        });
        gen.wrapWithBlock("complexType", gen.attributes("name", "ParameterType"), () -> {
            gen.addTag("attribute", "use", "required", "name", "name", "type", "string");
            gen.addTag("attribute", "use", "required", "name", "value", "type", "string");
        });
        gen.wrapWithBlock("complexType", gen.attributes("name", "ElementWithId"), () -> {
            gen.addTag("attribute", "use", "required", "name", "id", "type", "string");
        });
        XsdGeneratorUtils.processTags(registry.getEntityTags().values(), "entity", gen);
        JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "custom.xsd", xsdDir);
    }

}
