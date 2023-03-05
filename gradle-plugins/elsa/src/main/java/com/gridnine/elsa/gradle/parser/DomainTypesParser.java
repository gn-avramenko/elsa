/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser;

import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;

import java.io.File;
import java.nio.file.Files;

public class DomainTypesParser {
    public void updateRegistry(DomainTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("asset-attribute")){
            ParserUtils.addAttribute(registry.getAssetAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("document-attribute")){
            ParserUtils.addAttribute(registry.getDocumentAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("projection-attribute")){
            ParserUtils.addAttribute(registry.getProjectionAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("enum-attribute")){
            ParserUtils.addAttribute(registry.getEnumAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("enum-item-attribute")){
            ParserUtils.addAttribute(registry.getEnumItemAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("entity-tag")){
            ParserUtils.addTag(registry.getEntityTags(), child);
        }
        for(BuildXmlNode child : node.getChildren("database-tag")){
            ParserUtils.addTag(registry.getDatabaseTags(), child);
        }
    }
}
