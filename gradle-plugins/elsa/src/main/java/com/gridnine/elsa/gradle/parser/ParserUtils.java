/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser;

import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.AttributeType;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;

import java.util.List;
import java.util.Map;

public final class ParserUtils {


    public static void addAttribute(Map<String, AttributeDescription> assetAttributes, BuildXmlNode child) {
        var name = child.getAttribute("name");
        var attr = assetAttributes.computeIfAbsent(name, (it) ->{
            var res = new AttributeDescription();
            res.setName(name);
            return res;
        });
        attr.setType(AttributeType.valueOf(child.getAttribute("type")));
        attr.setDefaultValue(child.getAttribute("default-value"));
        var svs = child.getChildren("select-values");
        if(!svs.isEmpty()){
            for(BuildXmlNode sv : svs.get(0).getChildren("value")){
                attr.getSelectValues().add(sv.getValue());
            }
        }
    }

    public static void addTag(Map<String, TagDescription> entityTags, BuildXmlNode child) {
        var name = child.getAttribute("tag-name");
        var tag = entityTags.computeIfAbsent(name, (it) ->{
            var res = new TagDescription();
            res.setTagName(name);
            return res;
        });
        tag.setType(child.getAttribute("type"));
        tag.setObjectIdAttributeName(child.getAttribute("object-id-attribute-name"));
        for(BuildXmlNode attr: child.getChildren("attribute")){
            addAttribute(tag.getAttributes(), attr);
        }
        processGenerics(tag.getGenerics(), child);
    }

    private static void processGenerics(List<GenericDescription> generics, BuildXmlNode child) {
        for(BuildXmlNode attr: child.getChildren("generic")){
            var gen = new GenericDescription();
            gen.setType(attr.getAttribute("type"));
            gen.setId(attr.getAttribute("id"));
            gen.setObjectIdAttributeName(attr.getAttribute("object-id-attribute-name"));
            generics.add(gen);
            processGenerics(gen.getNestedGenerics(), attr);
        }
    }
}
