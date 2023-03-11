/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.custom;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.List;

public class CustomMetadataParser {

    public void updateRegistry(CustomMetaRegistry registry, SerializableMetaRegistry serializableMetaRegistry, List<File> sources) throws Exception {
        for(File it: sources){
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();
            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnumsIds(), serializableMetaRegistry.getEnums(), child, pr.localizations()));
             node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
        }
    }

}
