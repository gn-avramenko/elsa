/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.remoting;

import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.remoting.*;
import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;

import java.io.File;
import java.util.List;

public class RemotingMetaRegistryParser {

    public void updateMetaRegistry(RemotingMetaRegistry registry, List<File> sources) {
        sources.forEach(it -> BuildExceptionUtils.wrapException(() -> {
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();

            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnums(), child, pr.localizations()));
            node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntities(), child));
            node.getChildren("group").forEach(groupChild ->{
              var groupDescr = registry.getGroups().computeIfAbsent(CommonParserUtils.getIdAttribute(groupChild), RemotingGroupDescription::new);
              groupChild.getChildren("server-call").forEach(item -> {
                  var sc = groupDescr.getServerCalls().computeIfAbsent(CommonParserUtils.getIdAttribute(item),
                          RemotingServerCallDescription::new);
                  sc.setValidatable("true".equals(item.getAttribute("validatable")));
                  sc.setRequestClassName(parseEntity(registry, item.getChildren("request").get(0)));
                  sc.setResponseClassName(parseEntity(registry, item.getChildren("response").get(0)));
              });
                groupChild.getChildren("clientCall-call").forEach(item -> {
                    var sc = groupDescr.getClientCalls().computeIfAbsent(CommonParserUtils.getIdAttribute(item),
                            RemotingClientCallDescription::new);
                    sc.setRequestClassName(parseEntity(registry, item.getChildren("request").get(0)));
                    sc.setResponseClassName(parseEntity(registry, item.getChildren("response").get(0)));
                });
                groupChild.getChildren("subscription").forEach(item -> {
                    var sc = groupDescr.getSubscriptions().computeIfAbsent(CommonParserUtils.getIdAttribute(item),
                            RemotingSubscriptionDescription::new);
                    sc.setParameterClassName(parseEntity(registry, item.getChildren("parameter").get(0)));
                    sc.setEventClassName(parseEntity(registry, item.getChildren("event").get(0)));
                });
            });
            CommonParserUtils.updateImports(registry.getImports(), node);
        }));
    }

    private String parseEntity(RemotingMetaRegistry registry, BuildXmlNode elm) {
        var ett = CommonParserUtils.updateEntity(registry.getEntities(), elm);
        return ett.getId();
    }
}