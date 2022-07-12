/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.ui;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.XmlNode;
import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;

import java.util.Locale;
import java.util.Map;

public interface ViewTemplateParserHandlerCallback {
    void addEntity(EntityDescription ed);
    void addViewDescription(String id, XmlNode view, Map<String, Map<Locale,String>> localizations);
    ViewTemplateParserHandler getHandler(String tagName);
    UiMetaRegistry getFullRegistry();
}
