/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.types;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.XsdCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.XsdGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;

public class DomainXsdCodeGen {
    public void generate(DomainTypesRegistry registry, SerializableTypesRegistry sRegistry, File xsdDir, String customizationSuffix) throws Exception {
        var gen = new XsdCodeGenerator();
        gen.nameSpace("http://gridnine.com/elsa/meta-domain" + (customizationSuffix != null ? "-%s".formatted(customizationSuffix) : ""));
        gen.wrapWithBlock("element", gen.attributes("name", "domain"), () -> {
            gen.wrapWithBlock("complexType", gen.attributes(), () -> {

                gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                    gen.wrapWithBlock("element", gen.attributes("name", "document"), () -> {
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
                                        gen.wrapWithBlock("choice", gen.attributes(), () -> {
                                            gen.addTag("element", "name", "caption-expression", "type", "string");
                                            gen.addTag("element", "name", "localizable-caption-expression", "type", "string");
                                        });
                                    });
                                    gen.addTag("attribute", "name", "abstract", "type", "boolean", "default", "false");
                                    gen.addTag("attribute", "name", "extends", "type", "string");
                                    XsdGeneratorUtils.genCustomAttributes(registry.getDocumentAttributes(), "document", gen);
                                });
                            });
                        });

                    });
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
                                });
                            });
                        });

                    });
                    gen.wrapWithBlock("element", gen.attributes("name", "enum"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), () -> {
                                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                                        gen.addTag("element", "name", "enum-item", "type", "tns:IdWithParametersType", "maxOccurs", "unbounded", "minOccurs", "0");
                                        XsdGeneratorUtils.genCustomAttributes(registry.getEnumItemAttributes(), "enum-item", gen);
                                    });
                                    gen.addTag("attribute", "name", "abstract", "type", "boolean", "default", "false");
                                    gen.addTag("attribute", "name", "extends", "type", "string");
                                    XsdGeneratorUtils.genCustomAttributes(registry.getEnumAttributes(), "enum", gen);
                                });
                            });
                        });

                    });
                    gen.wrapWithBlock("element", gen.attributes("name", "projection"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:BaseProjectionType"), () -> {
                                    gen.addTag("attribute", "name", "document", "type", "string", "use", "required");
                                    XsdGeneratorUtils.genCustomAttributes(registry.getProjectionAttributes(), "enum", gen);
                                });
                            });
                        });

                    });
                    gen.wrapWithBlock("element", gen.attributes("name", "asset"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:BaseProjectionType"), () -> {
                                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                                        gen.wrapWithBlock("choice", gen.attributes(), () -> {
                                            gen.addTag("element", "name", "caption-expression", "type", "string");
                                            gen.addTag("element", "name", "localizable-caption-expression", "type", "string");
                                        });
                                    });
                                    gen.addTag("attribute", "name", "abstract", "type", "boolean", "default", "false");
                                    gen.addTag("attribute", "name", "extends", "type", "string");
                                    XsdGeneratorUtils.genCustomAttributes(registry.getDocumentAttributes(), "asset", gen);
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
        gen.wrapWithBlock("complexType", gen.attributes("name", "BaseProjectionType"), () -> {
            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                            gen.addTag("element", "name", "parameter", "type", "tns:ParameterType");
                        });
                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                            for (TagDescription tag : registry.getDatabaseTags().values()) {
                                gen.addTag("element", "name", tag.getTagName(), "type", "tns:database-tag-%s".formatted(tag.getTagName()));
                            }
                        });
                    });
                });
            });
        });
        XsdGeneratorUtils.processTags(registry.getEntityTags().values(), "entity", gen);
        XsdGeneratorUtils.processTags(registry.getDatabaseTags().values(), "database", gen);
        JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "domain.xsd", xsdDir);
    }

}
