/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.l10n;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;

import java.io.File;
import java.nio.file.Files;

public class L10nTypesParser {
    public void updateRegistry(L10nTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("parameter-type-tag")){
            CommonParserUtils.addTag(registry.getParameterTypeTags(), child);
        }
    }
}
