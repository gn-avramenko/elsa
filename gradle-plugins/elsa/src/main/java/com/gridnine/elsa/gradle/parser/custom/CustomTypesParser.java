/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.custom;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;

import java.io.File;
import java.nio.file.Files;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;

public class CustomTypesParser {
    public void updateRegistry(CustomTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("entity-tag")){
            CommonParserUtils.addTag(registry.getEntityTags(), child);
        }
    }
}
