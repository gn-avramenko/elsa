/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.l10n;

import com.gridnine.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.l10n.L10nMessageDescription;
import com.gridnine.elsa.meta.l10n.L10nMessagesBundleDescription;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class L10nMetadataParser {

    public void updateMetaRegistry(L10nMetaRegistry registry, List<File> sources) throws Exception {
        for(File source: sources) {
            MetaDataParsingResult pr = CommonParserUtils.parse(source);
            BuildXmlNode node = pr.node();
            var bundleDescription = registry.getBundles().computeIfAbsent(CommonParserUtils.getIdAttribute(node),
                    L10nMessagesBundleDescription::new);
            node.getChildren("message").forEach(child -> {
                var message = bundleDescription.getMessages().computeIfAbsent(CommonParserUtils.getIdAttribute(child),
                        L10nMessageDescription::new);
                node.getAttributes().forEach((key, value) -> {
                    if ("caption".equals(key)) {
                        message.getDisplayNames().put(Locale.ROOT, value);
                        return;
                    }
                    message.getAttributes().put(key, value);
                });
                var fullId = message.getId();
                var localizations = pr.localizations();
                if (fullId != null && localizations != null) {
                    String key = "%s.name".formatted(fullId);
                    var locs = localizations.get(key);
                    if (locs != null) {
                        locs.forEach((locale, name) -> {
                            message.getDisplayNames().put(locale, name);
                        });
                    }
                }
                child.getChildren("parameter").forEach(paramChild -> {
                    var param = message.getParameters().computeIfAbsent(CommonParserUtils.getIdAttribute(paramChild),
                            PropertyDescription::new);
                    param.setTagName(paramChild.getName());
                    CommonParserUtils.updateBaseElement(param, paramChild, "%s.%s".formatted(message.getId(), param.getId()), localizations);
                });
            });
        }
    }

}
