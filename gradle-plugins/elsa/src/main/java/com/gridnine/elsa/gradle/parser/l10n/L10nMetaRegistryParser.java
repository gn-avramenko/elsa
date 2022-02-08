/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.l10n;

import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessageParameterDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;

import java.io.File;
import java.util.List;

public class L10nMetaRegistryParser {

    public void updateMetaRegistry(L10nMetaRegistry registry, List<File> sources) {
        sources.forEach(it -> BuildExceptionUtils.wrapException(() -> {
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            BuildXmlNode node = pr.node();
            var bundleDescription = registry.getBundles().computeIfAbsent(CommonParserUtils.getIdAttribute(node),
                    L10nMessagesBundleDescription::new);
            node.getChildren("message").forEach(child ->{
                var message = bundleDescription.getMessages().computeIfAbsent(CommonParserUtils.getIdAttribute(child),
                        L10nMessageDescription::new);
                child.getChildren("parameter").forEach(paramChild ->{
                    var param = message.getParameters().computeIfAbsent(CommonParserUtils.getIdAttribute(paramChild),
                            L10nMessageParameterDescription::new);
                    param.setClassName(paramChild.getAttribute("class-name"));
                    param.setCollection("true".equals(paramChild.getAttribute("collection")));
                    param.setType(StandardValueType.valueOf(paramChild.getAttribute("type")));
                });
            });
        }));
    }


}
