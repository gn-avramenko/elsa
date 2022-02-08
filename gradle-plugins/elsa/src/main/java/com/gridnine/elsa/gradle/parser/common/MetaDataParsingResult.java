/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.common;

import com.gridnine.elsa.gradle.utils.BuildXmlNode;

import java.util.Locale;
import java.util.Map;

public record MetaDataParsingResult(BuildXmlNode node, Map<String, Map<Locale, String>> localizations) {
}
