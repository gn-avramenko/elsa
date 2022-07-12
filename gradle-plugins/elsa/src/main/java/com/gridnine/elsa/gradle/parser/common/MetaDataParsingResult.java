/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.common;

import com.gridnine.elsa.common.meta.common.XmlNode;

import java.util.Locale;
import java.util.Map;

public record MetaDataParsingResult(XmlNode node, Map<String, Map<Locale, String>> localizations) {
}
