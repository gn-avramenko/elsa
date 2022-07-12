/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui.template;

import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.ui.*;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Set;

public class JavaUiTemplateXsdGenerator {
    public static void generate(UiMetaRegistry registry, File destDir, String xsdFileName, String targetNamespace, Set<File> generatedFiles) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation domImpl = db.getDOMImplementation();
        Document document = domImpl.createDocument("http://www.w3.org/2001/XMLSchema",
                "schema", null);
        document.getDocumentElement().setAttribute("targetNamespace", targetNamespace);
        document.getDocumentElement().setAttribute("xmlns:tns", targetNamespace);
        document.getDocumentElement().setAttribute("elementFormDefault", "qualified");
        document.getDocumentElement().setAttribute("xmlns:common", "http://gridnine.com/elsa/meta-common");
        {
            var uiElm = addElement("element", document.getDocumentElement(), new attribute("name", "ui"));
            var uiElmCT = addElement("complexType", uiElm);
            var uiElmSeq = addElement("sequence", uiElmCT);
            var uiElmChoice = addElement("choice", uiElmSeq, new attribute("maxOccurs", "unbounded"),
                    new attribute("minOccurs", "0"));
            addElement("element", uiElmChoice, new attribute("name", "enum"), new attribute("type", "tns:EnumType"));
            for (UiViewTemplateDescription viewTemplateDescr : registry.getViewTemplates().values()) {
                addElement("element", uiElmChoice, new attribute("name", viewTemplateDescr.getId()), new attribute("type", "tns:%s".formatted(viewTemplateDescr.getId())));
            }
        }
        {
            var enumElm = addElement("complexType", document.getDocumentElement(), new attribute("name", "EnumType"));
            var cc = addElement("complexContent", enumElm);
            var ext = addElement("extension", cc, new attribute("base", "common:MetaElementType"));
            var sec = addElement("sequence", ext);
            var elm = addElement("element", sec, new attribute("name", "enum-item")
                    , new attribute("maxOccurs", "unbounded")
                    , new attribute("minOccurs", "0")
                    , new attribute("type", "common:EnumItemType"));
        }
        for (EnumDescription enumDescr : registry.getEnums().values()) {
            var enumElm = addElement("simpleType", document.getDocumentElement(), new attribute("name", JavaCodeGeneratorUtils.getSimpleName(enumDescr.getId())));
            var restrElm = addElement("restriction", enumElm, new attribute("base", "string"));
            enumDescr.getItems().values().forEach(ei -> {
                addElement("enumeration", restrElm, new attribute("value", ei.getId()));
            });
        }
        for (UiWidgetDescription widgetDescr : registry.getWidgets().values()) {
            var widgetElm = addElement("complexType", document.getDocumentElement(), new attribute("name", widgetDescr.getId()));
            for (UiAttributeDescription attrDescr : widgetDescr.getProperties().getAttributes().values()) {
                addAttribute(widgetElm, attrDescr);
            }
        }
        for (UiTemplateGroupDescription groupDescr : registry.getGroups().values()) {
            var groupElm = addElement("group", document.getDocumentElement(), new attribute("name", groupDescr.getId()));
            var choiceElm = addElement("choice", groupElm);
            for (String viewRef : groupDescr.getElements()) {
                addElement("element", choiceElm, new attribute("name", viewRef), new attribute("type", "tns:%s".formatted(viewRef)));
            }
        }
        for (UiViewTemplateDescription viewTemplateDescr : registry.getViewTemplates().values()) {
            var templateElm = addElement("complexType", document.getDocumentElement(), new attribute("name", viewTemplateDescr.getId()));
            var seqElm = addElement("sequence", templateElm);
            for (UiViewTemplatePropertyDescription propertyDescr : viewTemplateDescr.getContent().getProperties().values()) {
                addProperty(seqElm, propertyDescr);
            }
            for (UiViewTemplateCollectionDescription collectionDescr : viewTemplateDescr.getContent().getCollections().values()) {
                addCollection(seqElm, collectionDescr);
            }
            addElement("attribute", templateElm, new attribute("name", "id"), new attribute("type", "string"), new attribute("use", "required"));
            for (UiAttributeDescription attrDescr : viewTemplateDescr.getProperties().getAttributes().values()) {
                addAttribute(templateElm, attrDescr);
            }
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();
        var file = JavaCodeGeneratorUtils.saveIfDiffers(xmlString, xsdFileName, destDir);
        generatedFiles.add(file);
    }

    private static void addCollection(Element seqElm, UiViewTemplateCollectionDescription collectionDescr) {
        var attributes = new ArrayList<attribute>();
        var rootElm = seqElm;
        if(collectionDescr.getWrapperTagName() != null){
            var we = addElement(collectionDescr.getWrapperTagName(), seqElm);
            rootElm = addElement("complexType", we);
        }
        attributes.add(new attribute("name", collectionDescr.getElementTagName()));
        attributes.add(new attribute("minOccurs", "0"));
        attributes.add(new attribute("maxOccurs", "unbounded"));
        if (collectionDescr.getElementType() != UiViewTemplatePropertyType.ENTITY) {
            attributes.add(new attribute("type", getType(collectionDescr.getElementType(), collectionDescr.getElementClassName())));
        }
        var elementElm = addElement("element", rootElm, attributes.toArray(new attribute[0]));
        if (collectionDescr.getElementType() == UiViewTemplatePropertyType.ENTITY) {
            var cc = addElement("complexType", elementElm);
            var seqElm2 = addElement("sequence", cc);
            for (UiViewTemplatePropertyDescription propertyDescr2 : collectionDescr.getProperties().values()) {
                addProperty(seqElm2, propertyDescr2);
            }
            for (UiViewTemplateCollectionDescription collectionDescr2 : collectionDescr.getCollections().values()) {
                addCollection(seqElm2, collectionDescr2);
            }
            for (UiGroupDescription groupDescr : collectionDescr.getGroups()) {
                addGroup(seqElm2, groupDescr);
            }
            for (UiAttributeDescription attrDescr : collectionDescr.getAttributes().values()) {
                addAttribute(cc, attrDescr);
            }
        }
    }

    private static void addProperty(Element seqElm, UiViewTemplatePropertyDescription propertyDescr) {
        var attributes = new ArrayList<attribute>();
        attributes.add(new attribute("name", propertyDescr.getTagName()));
        if (!propertyDescr.isNonNullable()) {
            attributes.add(new attribute("minOccurs", "0"));
        }
        if (propertyDescr.getType() != UiViewTemplatePropertyType.ENTITY) {
            attributes.add(new attribute("type", getType(propertyDescr.getType(), propertyDescr.getClassName())));
        }
        var elementElm = addElement("element", seqElm, attributes.toArray(new attribute[0]));
        if (propertyDescr.getType() == UiViewTemplatePropertyType.ENTITY) {
            var cc = addElement("complexType", elementElm);
            var seqElm2 = addElement("sequence", cc);
            for (UiViewTemplatePropertyDescription propertyDescr2 : propertyDescr.getProperties().values()) {
                addProperty(seqElm2, propertyDescr2);
            }
            for (UiViewTemplateCollectionDescription collectionDescr2 : propertyDescr.getCollections().values()) {
                addCollection(seqElm2, collectionDescr2);
            }
            for (UiGroupDescription groupDescr : propertyDescr.getGroups()) {
                addGroup(seqElm2, groupDescr);
            }
            for (UiAttributeDescription attrDescr : propertyDescr.getAttributes().values()) {
                addAttribute(cc, attrDescr);
            }
        }
    }

    private static void addGroup(Element seqElm2, UiGroupDescription groupDescr) {
        var attributes = new ArrayList<attribute>();
        attributes.add(new attribute("ref", "tns:%s".formatted(groupDescr.getRef())));
        if(!groupDescr.isNonNullable()){
            attributes.add(new attribute("minOccurs", "0"));
        }
        addElement("group", seqElm2, attributes.toArray(new attribute[0]));
    }

    private static String getType(UiViewTemplatePropertyType type, String className) {
        return switch (type) {
            case ENUM -> "tns:%s".formatted(JavaCodeGeneratorUtils.getSimpleName(className));
            case BOOLEAN -> "boolean";
            case INT -> "integer";
            case STRING -> "string";
            case ENTITY -> throw new IllegalArgumentException("unsupported type " + type);
        };
    }

    private static void addAttribute(Element templateElm, UiAttributeDescription attrDescr) {
        var attrs = new ArrayList<attribute>();
        attrs.add(new attribute("name", attrDescr.getName()));
        attrs.add(switch (attrDescr.getType()) {
            case ENUM -> new attribute("type", "tns:%s".formatted(JavaCodeGeneratorUtils.getSimpleName(attrDescr.getClassName())));
            case BOOLEAN -> new attribute("type", "boolean");
            case INT -> new attribute("type", "integer");
            case STRING -> new attribute("type", "string");
        });
        if (attrDescr.getDefaultValue() != null) {
            attrs.add(new attribute("default", attrDescr.getDefaultValue()));
        }
        if (attrDescr.isNonNullable()) {
            attrs.add(new attribute("use", "required"));
        }
        addElement("attribute", templateElm, attrs.toArray(new attribute[0]));
    }

    private static Element addElement(String ui, Element documentElement, attribute... attributes) {
        var elm = documentElement.getOwnerDocument().createElement(ui);
        documentElement.appendChild(elm);
        for (attribute att : attributes) {
            elm.setAttribute(att.name, att.value);
        }
        return elm;
    }

    record attribute(String name, String value) {
    }
}
