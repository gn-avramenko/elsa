/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.domain;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.List;

public class DomainMetadataParser {

    public void updateRegistry(DomainMetaRegistry registry, SerializableMetaRegistry serializableMetaRegistry, List<File> sources) throws Exception {
        for(File it: sources){
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();
            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnumsIds(), serializableMetaRegistry.getEnums(), child, pr.localizations()));
            node.getChildren("document").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getDocumentsIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
            node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
            node.getChildren("projection").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getProjectionsIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
            node.getChildren("asset").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getAssetsIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
        }
    }

}
