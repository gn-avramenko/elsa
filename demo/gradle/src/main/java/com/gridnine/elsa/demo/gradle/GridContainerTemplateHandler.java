/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.gradle;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.common.XmlNode;
import com.gridnine.elsa.common.meta.ui.UiMetaRegistry;
import com.gridnine.elsa.common.meta.ui.UiViewMemberDescription;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.parser.ui.ViewTemplateParserHandler;
import com.gridnine.elsa.gradle.parser.ui.ViewTemplateParserHandlerCallback;

import java.util.*;

public class GridContainerTemplateHandler implements ViewTemplateParserHandler {
    @Override
    public String getTagName() {
        return "grid-container";
    }

    @Override
    public String getModelClassName(XmlNode node) {
        return "%sVM".formatted(node.getAttribute("id"));
    }

    @Override
    public String getConfigurationClassName(XmlNode node) {
        return "%sVC".formatted(node.getAttribute("id"));
    }

    @Override
    public String getValidationClassName(XmlNode node) {
        return "%sVV".formatted(node.getAttribute("id"));
    }

    @Override
    public void addEntities(XmlNode node, Map<String, Map<Locale, String>> localizations, ViewTemplateParserHandlerCallback callback) throws Exception {
        String id = node.getAttribute("id");
        var modelEtt = new EntityDescription("%sVM".formatted(id));
        var configEtt = new EntityDescription("%sVC".formatted(id));
        var validationEtt = new EntityDescription("%sVV".formatted(id));
        callback.addEntity(modelEtt);
        callback.addEntity(configEtt);
        callback.addEntity(validationEtt);
        Map<String, Map<Locale, String>> l10ns = new LinkedHashMap<>();
        for (XmlNode rowElm : node.getChildren("row")) {
            for (XmlNode cellElm : rowElm.getChildren("cell")) {
                var cellId = cellElm.getAttribute("id");
                String captionId = cellElm.getAttribute("caption");
                if(localizations.containsKey(captionId)){
                    l10ns.put(cellId, localizations.get(captionId));
                }
                var containerRef = cellElm.getAttribute("container-ref");
                if(containerRef != null){
                    var view = callback.getFullRegistry().getViews().get(containerRef);
                    if(view != null){
                        var handler2 = callback.getHandler(view.getView().getName());
                        var vmProp = new StandardPropertyDescription(cellId);
                        modelEtt.getProperties().put(vmProp.getId(), vmProp);
                        vmProp.setType(StandardValueType.ENTITY);
                        vmProp.setClassName(handler2.getModelClassName(view.getView()));
                        var vcProp = new StandardPropertyDescription(cellId);
                        configEtt.getProperties().put(vcProp.getId(), vcProp);
                        vcProp.setType(StandardValueType.ENTITY);
                        vcProp.setClassName(handler2.getConfigurationClassName(view.getView()));
                        var vvProp = new StandardPropertyDescription(cellId);
                        validationEtt.getProperties().put(vvProp.getId(), vvProp);
                        vvProp.setType(StandardValueType.ENTITY);
                        vvProp.setClassName(handler2.getValidationClassName(view.getView()));
                    }
                    continue;
                }
                var widgetElm = cellElm.getChildren().isEmpty() ? null : cellElm.getChildren().get(0);
                if (widgetElm != null) {
                    var vmProp = new StandardPropertyDescription(cellId);
                    modelEtt.getProperties().put(vmProp.getId(), vmProp);
                    var vcProp = new StandardPropertyDescription(cellId);
                    configEtt.getProperties().put(vcProp.getId(), vcProp);
                    var vvProp = new StandardPropertyDescription(cellId);
                    validationEtt.getProperties().put(vvProp.getId(), vvProp);
                    if (ViewTemplateParserHandler.isViewTemplate(widgetElm, callback)) {
                        var handler = callback.getHandler(widgetElm.getName());
                        vmProp.setClassName(handler.getModelClassName(widgetElm));
                        vmProp.setType(StandardValueType.ENTITY);
                        vcProp.setClassName(handler.getConfigurationClassName(widgetElm));
                        vcProp.setType(StandardValueType.ENTITY);
                        vvProp.setClassName(handler.getValidationClassName(widgetElm));
                        vvProp.setType(StandardValueType.ENTITY);
                        handler.addEntities(widgetElm, localizations, callback);
                        continue;
                    }
                    ViewTemplateParserHandler.updateModelWidgetProperty(widgetElm, vmProp, callback);
                    ViewTemplateParserHandler.updateConfigurationWidgetProperty(widgetElm, vcProp, callback);
                    ViewTemplateParserHandler.updateValidationWidgetProperty(widgetElm, vvProp, callback);
                }
            }
        }
        callback.addViewDescription(id, node, l10ns);
    }

    @Override
    public List<UiViewMemberDescription> getViewMembers(XmlNode node, Map<String, ViewTemplateParserHandler> handlers, UiMetaRegistry registry) {
        List<UiViewMemberDescription> result = new ArrayList<>();
        for (XmlNode rowElm : node.getChildren("row")) {
            for (XmlNode cellElm : rowElm.getChildren("cell")) {
                var cellId = cellElm.getAttribute("id");
                var containerRef = cellElm.getAttribute("container-ref");
                if(containerRef != null){
                    var item = new UiViewMemberDescription();
                    item.setId(cellId);
                    item.setWidgetClass(JavaCodeGeneratorUtils.getSimpleName(containerRef));
                    result.add(item);
                    continue;
                }
                var widgetElm = cellElm.getChildren().isEmpty() ? null : cellElm.getChildren().get(0);
                if(widgetElm != null){
                    if(handlers.containsKey(widgetElm.getName())){
                        var handler = handlers.get(widgetElm.getName());
                        var item = new UiViewMemberDescription();
                        item.setId(cellId);
                        item.setWidgetClass(JavaCodeGeneratorUtils.getSimpleName(handler.getId(widgetElm)));
                        result.add(item);
                        continue;
                    }
                    var widget = registry.getWidgets().get(widgetElm.getName());
                    var item = new UiViewMemberDescription();
                    item.setId(cellId);
                    item.setWidgetClass(widget.getTsClassName());
                    result.add(item);
                }
            }
        }
        return result;
    }

    @Override
    public String getId(XmlNode viewNode) {
        return viewNode.getAttribute("id");
    }

    @Override
    public String getWidgetClassName(XmlNode node) {
        var id = JavaCodeGeneratorUtils.getSimpleName(getId(node));
        return "GridLayoutContainer<%sVM, %sVC, %sVV>".formatted(id, id, id);
    }

    @Override
    public void updateImports(Set<String> additionalEntities, XmlNode node, UiMetaRegistry ftr, Map<String, ViewTemplateParserHandler> handlers) {
        additionalEntities.add("GridLayoutContainer");
        for (XmlNode rowElm : node.getChildren("row")) {
            for (XmlNode cellElm : rowElm.getChildren("cell")) {
                var cellId = cellElm.getAttribute("id");
                var containerRef = cellElm.getAttribute("container-ref");
                if(containerRef != null){
                    var contnainerNode = ftr.getViews().get(containerRef);
                    var handler2 = handlers.get(contnainerNode.getView().getName());
                    additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(handler2.getId(contnainerNode.getView())));
                    additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(handler2.getModelClassName(contnainerNode.getView())));
                    additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(handler2.getConfigurationClassName(contnainerNode.getView())));
                    additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(handler2.getValidationClassName(contnainerNode.getView())));
                    continue;
                }
                var widgetElm = cellElm.getChildren().isEmpty() ? null : cellElm.getChildren().get(0);
                if(widgetElm != null){
                    if(handlers.containsKey(widgetElm.getName())){
                        var handler = handlers.get(widgetElm.getName());
                        handler.updateImports(additionalEntities, widgetElm, ftr, handlers);
                        continue;
                    }
                    var widget = ftr.getWidgets().get(widgetElm.getName());
                    additionalEntities.add(widget.getTsClassName());
                }
            }
        }
    }

    @Override
    public List<XmlNode> getAllViewNodes(XmlNode node, Map<String, ViewTemplateParserHandler> handlers) {
        var result = new ArrayList<XmlNode>();
        result.add(node);
        for (XmlNode rowElm : node.getChildren("row")) {
            for (XmlNode cellElm : rowElm.getChildren("cell")) {
                var cellId = cellElm.getAttribute("id");
                var widgetElm = cellElm.getChildren().isEmpty() ? null : cellElm.getChildren().get(0);
                if(widgetElm != null){
                    if(handlers.containsKey(widgetElm.getName())){
                        var handler = handlers.get(widgetElm.getName());
                        result.addAll(handler.getAllViewNodes(widgetElm, handlers));
                        continue;
                    }
                }
            }
        }
        return result;
    }
}
