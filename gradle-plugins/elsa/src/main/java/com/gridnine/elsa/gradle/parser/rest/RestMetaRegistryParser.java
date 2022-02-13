/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.rest;

import com.gridnine.elsa.common.meta.rest.RestDescription;
import com.gridnine.elsa.common.meta.rest.RestGroupDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestOperationDescription;
import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;

import java.io.File;
import java.util.List;

public class RestMetaRegistryParser {

    public void updateMetaRegistry(RestMetaRegistry registry, List<File> sources) {
        sources.forEach(it -> BuildExceptionUtils.wrapException(() -> {
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();
            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnums(), child, pr.localizations()));
            node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntities(), child));
            var restId = CommonParserUtils.getIdAttribute(node);
            registry.getRests().computeIfAbsent(restId, RestDescription::new);
            node.getChildren("group").forEach(child ->{
                var groupId = "%s_%s".formatted(restId, CommonParserUtils.getIdAttribute(child));
                var group = registry.getGroups().computeIfAbsent(groupId, RestGroupDescription::new);
                child.getChildren("operation").forEach(opChild ->{
                    var operationId = "%s_%s".formatted(groupId, CommonParserUtils.getIdAttribute(opChild));
                    var operation = registry.getOperations().computeIfAbsent(operationId, RestOperationDescription::new);
                    operation.setHandler(opChild.getAttribute("handler"));
                    var request = opChild.getChildren("request").get(0);
                    var requestEtt = CommonParserUtils.updateEntity(registry.getEntities(), request);
                    var response = opChild.getChildren("response").get(0);
                    var responseEtt = CommonParserUtils.updateEntity(registry.getEntities(), response);
                    operation.setRequestEntity(requestEtt.getId());
                    operation.setGroupId(groupId);
                    operation.setValidatable("true".equals(opChild.getAttribute("validatable")));
                    operation.setResponseEntity(responseEtt.getId());
                });
            });
        }));
    }
}
