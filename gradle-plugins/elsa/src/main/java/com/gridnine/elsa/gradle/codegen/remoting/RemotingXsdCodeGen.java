/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.XsdCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.XsdGeneratorUtils;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Collections;

public class RemotingXsdCodeGen {
    public void generate(RemotingTypesRegistry registry, SerializableTypesRegistry sRegistry, File xsdDir, String customizationSuffix) throws Exception {
        var gen = new XsdCodeGenerator();
        gen.nameSpace("http://gridnine.com/elsa/meta-remoting" + (customizationSuffix != null ? "-%s".formatted(customizationSuffix) : ""));
        gen.wrapWithBlock("element", gen.attributes("name", "remoting"), () -> {
            gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                    gen.addTag("element", "name", "entity", "type","tns:EntityType");
                    gen.wrapWithBlock("element", gen.attributes("name", "enum"), () -> {
                        gen.wrapWithBlock("complexType", gen.attributes(), () -> {
                            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), () -> {
                                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                                        XsdGeneratorUtils.genEnumItemElement(Collections.emptyMap(), gen);
                                    });
                                });
                            });
                        });
                    });
                    gen.wrapWithBlock("element", gen.attributes("name", "group"), () ->{
                        gen.wrapWithBlock("complexType", gen.attributes(), ()->{
                            gen.wrapWithBlock("complexContent", gen.attributes(), ()->{
                                gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), ()->{
                                    gen.wrapWithBlock("sequence", gen.attributes(), ()->{
                                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), ()->{
                                            gen.wrapWithBlock("element", gen.attributes("name", "server-call"), ()->{
                                                gen.wrapWithBlock("complexType", gen.attributes(), ()->{
                                                    gen.wrapWithBlock("complexContent", gen.attributes(), ()->{
                                                        gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), ()->{
                                                            gen.wrapWithBlock("sequence", gen.attributes(), ()->{
                                                                gen.addTag("element", "name", "request", "type", "tns:EntityType", "minOccurs", "0");
                                                                gen.addTag("element", "name", "response", "type", "tns:EntityType");
                                                            });
                                                            XsdGeneratorUtils.genCustomAttributes(registry.getServerCallAttributes(), "server-call", gen);
                                                        });
                                                    });
                                                });
                                            });
                                            gen.wrapWithBlock("element", gen.attributes("name", "download"), ()->{
                                                gen.wrapWithBlock("complexType", gen.attributes(), ()->{
                                                    gen.wrapWithBlock("complexContent", gen.attributes(), ()->{
                                                        gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), ()->{
                                                            gen.wrapWithBlock("sequence", gen.attributes(), ()->{
                                                                gen.addTag("element", "name", "request", "type", "tns:EntityType", "minOccurs", "0");
                                                            });
                                                            XsdGeneratorUtils.genCustomAttributes(registry.getDownloadAttributes(), "download", gen);
                                                        });
                                                    });
                                                });
                                            });
                                            gen.wrapWithBlock("element", gen.attributes("name", "upload"), ()->{
                                                gen.wrapWithBlock("complexType", gen.attributes(), ()->{
                                                    gen.wrapWithBlock("complexContent", gen.attributes(), ()->{
                                                        gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), ()->{
                                                            gen.wrapWithBlock("sequence", gen.attributes(), ()->{
                                                                gen.addTag("element", "name", "request", "type", "tns:EntityType", "minOccurs", "0");
                                                            });
                                                            XsdGeneratorUtils.genCustomAttributes(registry.getUploadAttributes(), "upload", gen);
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
                gen.addTag("attribute", "use", "required", "name", "id", "type", "string");
                XsdGeneratorUtils.genCustomAttributes(registry.getRemotingAttributes(), "remoting", gen);
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
        gen.wrapWithBlock("complexType", gen.attributes("name", "EntityType"), () -> {
            gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                gen.wrapWithBlock("extension", gen.attributes("base", "tns:ElementWithId"), () -> {
                    gen.wrapWithBlock("sequence", gen.attributes(), () -> {
                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                            gen.addTag("element", "name", "parameter", "type", "tns:ParameterType");
                        });
                        gen.wrapWithBlock("choice", gen.attributes("maxOccurs", "unbounded", "minOccurs", "0"), () -> {
                            for (TagDescription tag : registry.getEntityTags().values()) {
                                gen.addTag("element", "name", tag.getTagName(), "type", "tns:remoting-tag-%s".formatted(tag.getTagName()));
                            }
                        });
                    });
                    gen.addTag("attribute", "name", "abstract", "type", "boolean", "default", "false");
                    gen.addTag("attribute", "name", "extends", "type", "string");
                });
            });
        });
        XsdGeneratorUtils.processTags(registry.getEntityTags().values(), "remoting", gen);
        JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "remoting.xsd", xsdDir);
    }

}
