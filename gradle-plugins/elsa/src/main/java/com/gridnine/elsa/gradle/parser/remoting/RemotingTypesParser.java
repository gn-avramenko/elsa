/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.remoting;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class RemotingTypesParser {
    public void updateRegistry(RemotingTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("remoting-attribute")){
            CommonParserUtils.addAttribute(registry.getRemotingAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("group-attribute")){
            CommonParserUtils.addAttribute(registry.getGroupAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("server-call-attribute")){
            CommonParserUtils.addAttribute(registry.getServerCallAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("server-subscription-attribute")){
            CommonParserUtils.addAttribute(registry.getServerSubscriptionAttributes(), child);
        }
        for(BuildXmlNode child : node.getChildren("entity-tag")){
            CommonParserUtils.addTag(registry.getEntityTags(), child);
        }
    }
}
