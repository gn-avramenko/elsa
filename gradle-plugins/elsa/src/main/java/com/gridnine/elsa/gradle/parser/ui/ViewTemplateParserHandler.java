/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.ui;

import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.common.XmlNode;
import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.common.meta.ui.UiViewMemberDescription;
import com.gridnine.elsa.gradle.parser.ui.template.UiTemplateMetaRegistryParser;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface ViewTemplateParserHandler {

    static boolean isViewTemplate(XmlNode node, ViewTemplateParserHandlerCallback callback) {
        return callback.getHandler(node.getName()) != null;
    }

    static void updateModelWidgetProperty(XmlNode widgetNode, StandardPropertyDescription descr, ViewTemplateParserHandlerCallback callback) {
        var widget = callback.getFullRegistry().getWidgets().get(widgetNode.getName());
        descr.setType(UiTemplateMetaRegistryParser.toStandardType(widget.getModel().getType()));
        descr.setClassName(getClassName(descr.getType(), widget.getModel().getClassName()));
    }

    static void updateConfigurationWidgetProperty(XmlNode widgetNode, StandardPropertyDescription descr, ViewTemplateParserHandlerCallback callback) {
        var widget = callback.getFullRegistry().getWidgets().get(widgetNode.getName());
        descr.setType(UiTemplateMetaRegistryParser.toStandardType(widget.getConfiguration().getType()));
        descr.setClassName(getClassName(descr.getType(), widget.getConfiguration().getClassName()));
    }

    static void updateValidationWidgetProperty(XmlNode widgetNode, StandardPropertyDescription descr, ViewTemplateParserHandlerCallback callback) {
        var widget = callback.getFullRegistry().getWidgets().get(widgetNode.getName());
        descr.setType(UiTemplateMetaRegistryParser.toStandardType(widget.getValidation().getType()));
        descr.setClassName(getClassName(descr.getType(), widget.getModel().getClassName()));
    }

    private static String getClassName(StandardValueType type, String className){
        return switch (type){
            case ENUM, CLASS, ENTITY -> className;
            default -> null;
        };
    }

    String getTagName();
    String getModelClassName(XmlNode node);
    String getConfigurationClassName(XmlNode node);
    String getValidationClassName(XmlNode node);
    void addEntities(XmlNode node, Map<String, Map<Locale, String>> localizations, ViewTemplateParserHandlerCallback callback) throws Exception;
    List<UiViewMemberDescription> getViewMembers(XmlNode node, Map<String, ViewTemplateParserHandler> handlers, UiMetaRegistry registry);
    String getId(XmlNode viewNode);
    String getWidgetClassName(XmlNode node);
    void updateImports(Set<String> additionalEntities, XmlNode name, UiMetaRegistry ftr, Map<String, ViewTemplateParserHandler> handler);

    List<XmlNode> getAllViewNodes(XmlNode view, Map<String, ViewTemplateParserHandler> handlers);
}
