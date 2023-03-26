/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.remoting;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.remoting.RemotingServerSubscriptionDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class RemotingMetadataParser {

    public void updateRegistry(RemotingMetaRegistry registry, SerializableMetaRegistry serializableMetaRegistry, List<File> sources) throws Exception {
        for(File it: sources){
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();
            String id = CommonParserUtils.getIdAttribute(node);
            var rd = registry.getRemotings().computeIfAbsent(id, (rid) ->{
                RemotingDescription red = new RemotingDescription();
                red.setId(rid);
                return red;
            });
            CommonParserUtils.updateBaseElement(rd, node, id, pr.localizations());
            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnumsIds(), serializableMetaRegistry.getEnums(), child, pr.localizations()));
            node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), child, pr.localizations()));
            for(BuildXmlNode groupElm: node.getChildren("group")){
                String gid = CommonParserUtils.getIdAttribute(groupElm);
                var rgd = rd.getGroups().computeIfAbsent(gid, (gi) -> {
                    RemotingGroupDescription rgds = new RemotingGroupDescription();
                    rgds.setId(gi);
                    return rgds;
                });
                CommonParserUtils.updateBaseElement(rgd, groupElm, "%s.%s".formatted(id, gid), pr.localizations());
                for(BuildXmlNode serverCallElm:groupElm.getChildren("server-call")){
                    String scid = CommonParserUtils.getIdAttribute(serverCallElm);
                    var scd = rgd.getServerCalls().computeIfAbsent(scid, (s)->{
                        var sgds = new RemotingServerCallDescription();
                        sgds.setId(s);
                        return sgds;
                    });
                    CommonParserUtils.updateBaseElement(scd, serverCallElm, "%s.%s.%s".formatted(id, gid, scid), pr.localizations());
                    BuildXmlNode requestElm = serverCallElm.getFirstChild("request");
                    if(requestElm != null){
                        scd.setRequestClassName(CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), requestElm, pr.localizations()));
                    }
                    BuildXmlNode responseElm = serverCallElm.getFirstChild("response");
                    if(responseElm != null){
                        scd.setResponseClassName(CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), responseElm, pr.localizations()));
                    }
                }
                for(BuildXmlNode serverCallElm:groupElm.getChildren("server-subscription")){
                    String scid = CommonParserUtils.getIdAttribute(serverCallElm);
                    var scd = rgd.getServerSubscriptions().computeIfAbsent(scid, (s)->{
                        var sgds = new RemotingServerSubscriptionDescription();
                        sgds.setId(s);
                        return sgds;
                    });
                    CommonParserUtils.updateBaseElement(scd, serverCallElm, "%s.%s.%s".formatted(id, gid, scid), pr.localizations());
                    BuildXmlNode parameterElm = serverCallElm.getFirstChild("subscription-parameter");
                    if(parameterElm != null){
                        scd.setParameterClassName(CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), parameterElm, pr.localizations()));
                    }
                    BuildXmlNode eventElm = serverCallElm.getFirstChild("event");
                    if(eventElm != null){
                        scd.setEventClassName(CommonParserUtils.updateEntity(registry.getEntitiesIds(), serializableMetaRegistry.getEntities(), eventElm, pr.localizations()));
                    }
                }
            }
        }
    }

}
