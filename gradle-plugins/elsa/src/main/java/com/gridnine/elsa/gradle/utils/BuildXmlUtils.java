/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public final class BuildXmlUtils {
    public static BuildXmlNode parseXml(byte[] content) throws Exception {
        var result = new BuildXmlNode();
        var db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc;
        try {
            doc = db.parse(new ByteArrayInputStream(content));
        } finally {
            db.reset();
        }
        updateElm(result, doc.getDocumentElement());
        return result;
    }

    private static void updateElm(BuildXmlNode result, Element elm) {
        result.setName(elm.getTagName());
        var attributes = elm.getAttributes();
        var attributeLength = attributes.getLength();
        for (int i = 0; i < attributeLength; i++) {
            var item = attributes.item(i);
            result.getAttributes().put(item.getNodeName(), item.getNodeValue());
        }
        var children = elm.getChildNodes();
        var childrenLength = children.getLength();
        for (int i = 0; i < childrenLength; i++) {
            var child = children.item(i);
            if (child != null) {
                switch (child.getNodeType()) {
                    case Node.TEXT_NODE, Node.CDATA_SECTION_NODE -> result.setValue(child.getNodeValue());
                    case Node.ELEMENT_NODE -> {
                        var childNode = new BuildXmlNode();
                        updateElm(childNode, (Element) child);
                        result.getChildren().add(childNode);
                    }
                }
            }
        }
    }

    private BuildXmlUtils() {
    }
}
