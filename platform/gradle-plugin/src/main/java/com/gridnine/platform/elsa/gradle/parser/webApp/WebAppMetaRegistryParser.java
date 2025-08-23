/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.gradle.parser.webApp;

import com.gridnine.platform.elsa.gradle.meta.common.StandardCollectionDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardPropertyDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.common.XmlNode;
import com.gridnine.platform.elsa.gradle.meta.webApp.*;
import com.gridnine.platform.elsa.gradle.parser.common.CommonParserUtils;
import com.gridnine.platform.elsa.gradle.parser.common.MetaDataParsingResult;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.List;

public class WebAppMetaRegistryParser {

    public void updateMetaRegistry(WebAppMetaRegistry registry, List<File> sources) {
        sources.forEach(it -> BuildExceptionUtils.wrapException(() -> {
            MetaDataParsingResult pr = CommonParserUtils.parse(it);
            XmlNode node = pr.node();
            node.getChildren("enum").forEach(child ->
                    CommonParserUtils.updateEnum(registry.getEnums(), child, pr.localizations()));
            node.getChildren("entity").forEach(child ->
                    CommonParserUtils.updateEntity(registry.getEntities(), child));
            node.getChildren("element").forEach(child -> {
                        processElement(child, registry);

                    }
            );
        }));
    }

    private void processElement(XmlNode child, WebAppMetaRegistry registry) {
        String tagName = child.getName();
        String className = child.getAttribute("class-name");
        String id = child.getAttribute("id");
        var dd = registry.getElements().computeIfAbsent(className,
                (cl) -> {
                    return switch (tagName) {
                        case "container" -> new ContainerWebElementDescription(id, cl);
                        case "button" -> new ButtonWebElementDescription(id, cl);
                        case "select" -> new SelectWebElementDescription(id, cl);
                        case "router" -> new RouterWebElementDescription(id, cl);
                        case "text-area" -> new TextAreaWebElementDescription(id, cl);
                        case "text-field" -> new TextFieldWebElementDescription(id, cl);
                        case "autocomplete" -> new AutocompleteWebElementDescription(id, cl);
                        case "label" -> new LabelWebElementDescription(id, cl);
                        case "custom" -> new CustomWebElementDescription(id, cl);
                        default -> throw new IllegalArgumentException("unsupported tag name " + tagName);
                    };
                });
        updateBaseProperties(dd, child);
        switch (dd.getType()) {
            case CONTAINER -> {
                ContainerWebElementDescription cc = (ContainerWebElementDescription) dd;
                var flexDirectionStr = child.getAttribute("flex-direction");
                cc.setFlexDirection(flexDirectionStr == null ? null : ContainerFlexDirection.valueOf(flexDirectionStr));
                var children = child.getFirstChild("children");
                if (children != null) {
                    processElement(children, registry);
                }
            }
            case BUTTON, SELECT, ROUTER, TEXT_AREA, TEXT_FIELD, AUTOCOMPLETE, LABEL, CUSTOM -> {
                //noops
            }
            case TABLE -> {
                TableWebElementDescription td = (TableWebElementDescription) dd;
                td.setColumnDescriptionExtension(getElementExtension(child.getFirstChild("column-description-extension")));
            }

        }
    }

    private void updateBaseProperties(BaseWebElementDescription dd, XmlNode child) {
        dd.setPropertiesExtension(getElementExtension(child.getFirstChild("properties-extension")));
        ;
        dd.setStateExtension(getElementExtension(child.getFirstChild("state-extension")));
        var scs = child.getFirstChild("additional-commands-from-server");
        if (scs != null) {
            scs.getChildren().forEach(command -> {
                //dd.getCommandsFromServer().add(CommonParserUtils.getIdAttribute(command));
            });
        }
        var ccs = child.getFirstChild("additional-commands-from-client");
        if (ccs != null) {
            ccs.getChildren().forEach(command -> {
                //dd.getCommandsFromClient().add(CommonParserUtils.getIdAttribute(command));
            });
        }
    }

    private ElementExtension getElementExtension(XmlNode child) {
        if (child == null) {
            return null;
        }
        var result = new ElementExtension();
        child.getChildren("property").forEach(prop -> {
            var id = CommonParserUtils.getIdAttribute(prop);
            var pd = new StandardPropertyDescription(id);
            pd.setClassName(prop.getAttribute("class-name"));
            pd.setNonNullable("true".equals(prop.getAttribute("non-nullable")));
            pd.setType(StandardValueType.valueOf(prop.getAttribute("type")));
            result.getProperties().put(id, pd);
        });
        child.getChildren("collection").forEach(coll -> {
            var id = CommonParserUtils.getIdAttribute(coll);
            var cd = new StandardCollectionDescription(id);
            cd.setElementClassName(coll.getAttribute("element-class-name"));
            cd.setElementType(StandardValueType.valueOf(coll.getAttribute("element-type")));
            cd.setUnique("true".equals(coll.getAttribute("unique")));
            result.getCollections().put(id, cd);
        });
        return result;
    }

}
