/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class XsdGeneratorUtils {


    public static void processTags(Collection<TagDescription> values, String prefix, XsdCodeGenerator gen) throws Exception {
        for (TagDescription tag : values) {
            gen.wrapWithBlock("complexType", gen.attributes("name", "%s-tag-%s".formatted(prefix, tag.getTagName())), () -> {
                gen.wrapWithBlock("complexContent", gen.attributes(), () -> {
                    gen.wrapWithBlock("extension", gen.attributes("base", "tns:IdWithParametersType"), () -> {
                        String typeName = tag.getType();
                        if (typeName.equals("ENTITY") || typeName.equals("ENUM")) {
                            gen.addTag("attribute", "use", "required", "name", tag.getObjectIdAttributeName(), "type", "string");
                        } else {
                            processGenerics(tag.getGenerics(), gen);
                        }
                        for (AttributeDescription attr : tag.getAttributes().values()) {
                            gen.addTag("attribute", "name", attr.getName(), "type", "string");
                        }
                    });
                });
            });
        }
    }

    public static void genCustomAttributes(Map<String, AttributeDescription> documentAttributes, String document, XsdCodeGenerator gen) {
        for (AttributeDescription attr : documentAttributes.values()) {
            var atrs = gen.attributes("name", attr.getName());
            atrs.put("type", switch (attr.getType()) {
                case BOOLEAN -> "boolean";
                case STRING -> "string";
                case SELECT -> "tns:%s-attribute-%s".formatted(document, attr.getName());
            });
            if (attr.getDefaultValue() != null) {
                atrs.put("default", attr.getDefaultValue());
            }
            gen.addTag("attribute", true, atrs);
        }
    }

    public static void processGenerics(List<GenericDescription> generics, XsdCodeGenerator gen) {
        for (GenericDescription generic : generics) {
            if (generic.getType().equals("ENTITY") || generic.getType().equals("ENUM")) {
                gen.addTag("attribute", "use", "required", "name", generic.getObjectIdAttributeName(), "type", "string");
                continue;
            }
            processGenerics(generic.getNestedGenerics(), gen);
        }
    }
}
