/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.domain;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class DomainTypesParser {
    public void updateRegistry(DomainTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("asset-attribute")){
            CommonParserUtils.addAttribute(registry.getAssetAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("document-attribute")){
            CommonParserUtils.addAttribute(registry.getDocumentAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("projection-attribute")){
            CommonParserUtils.addAttribute(registry.getProjectionAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("enum-attribute")){
            CommonParserUtils.addAttribute(registry.getEnumAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("enum-item-attribute")){
            CommonParserUtils.addAttribute(registry.getEnumItemAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("entity-tag")){
            CommonParserUtils.addTag(registry.getEntityTags(), child);
        }
        for(BuildXmlNode child : node.getChildren("database-tag")){
            addTag(registry.getDatabaseTags(), child);
        }
    }

    private static void addTag(Map<String, DatabaseTagDescription> entityTags, BuildXmlNode child) {
        var name = child.getAttribute("tag-name");
        var tag = entityTags.computeIfAbsent(name, (it) ->{
            var res = new DatabaseTagDescription();
            res.setTagName(name);
            return res;
        });
        tag.setType(child.getAttribute("type"));
        tag.setObjectIdAttributeName(child.getAttribute("object-id-attribute-name"));
        for(BuildXmlNode attr: child.getChildren("attribute")){
            CommonParserUtils.addAttribute(tag.getAttributes(), attr);
        }
        tag.setHasCollectionSupport("true".equals(child.getAttribute("has-collection-support")));
        tag.setHasEqualitySupport("true".equals(child.getAttribute("has-equility-support")));
        tag.setHasComparisonSupport("true".equals(child.getAttribute("has-comparison-support")));
        tag.setHasNumberOperationsSupport("true".equals(child.getAttribute("has-number-opertations-support")));
        tag.setHasSortSupport("true".equals(child.getAttribute("has-sort-support")));
        tag.setHasStringOperationsSupport("true".equals(child.getAttribute("has-string-operations-support")));
        tag.setSearchQueryArgumentType(child.getAttribute("search-query-argument-type"));
        CommonParserUtils.processGenerics(tag.getGenerics(), child);
    }
}
