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

import com.gridnine.platform.elsa.gradle.meta.common.*;
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
            node.getChildren().forEach(child -> {
                if("enum".equals(child.getName())){
                    CommonParserUtils.updateEnum(registry.getEnums(), child, pr.localizations());
                } else if("entity".equals(child.getName())){
                    CommonParserUtils.updateEntity(registry.getEntities(), child);
                } else {
                    processElement(child, registry);
                }
            });
        }));
        {
            var flexDirectionEnum = new EnumDescription("com.gridnine.platform.elsa.webApp.common.FlexDirection");
            {
                var item = new EnumItemDescription("ROW");
                flexDirectionEnum.getItems().put(item.getId(), item);
            }
            {
                var item = new EnumItemDescription("COLUMN");
                flexDirectionEnum.getItems().put(item.getId(), item);
            }
            registry.getEnums().put(flexDirectionEnum.getId(), flexDirectionEnum);
        }
    }

    private void processElement(XmlNode child, WebAppMetaRegistry registry) {
        String tagName = child.getName();
        String className = child.getAttribute("class-name");
        String id = child.getAttribute("id");
        var dd = registry.getElements().computeIfAbsent(className,
                (cl) -> {
                    return switch (tagName) {
                        case "modal" -> new ModalWebElementDescription(id, cl);
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
            case MODAL -> {
                ModalWebElementDescription cc = (ModalWebElementDescription) dd;
                var children = child.getFirstChild("children");
                if (children != null) {
                    processElement(children, registry);
                }
            }
            case CONTAINER -> {
                ContainerWebElementDescription cc = (ContainerWebElementDescription) dd;
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
        var ce = WebAppMetadataHelper.extendWithStandardProperties(dd);
        {
            var cn = "%sConfiguration".formatted(ce.getClassName());
            var ed = registry.getEntities().computeIfAbsent(cn, EntityDescription::new);
            ed.getParameters().put("no-serialization", "true");
            ed.getProperties().putAll(ce.getServerManagedState().getProperties());
            ed.getCollections().putAll(ce.getServerManagedState().getCollections());
        }
    }

    private void updateBaseProperties(BaseWebElementDescription dd, XmlNode child) {
        var state = getElementExtension(child.getFirstChild("server-manages-state"));
        if(state != null) {
            dd.getServerManagedState().getProperties().putAll(state.getProperties());
            dd.getServerManagedState().getCollections().putAll(state.getCollections());
        }
        var scs = child.getFirstChild("commands-from-server");
        if (scs != null) {
            scs.getChildren().forEach(command -> {
                //dd.getCommandsFromServer().add(CommonParserUtils.getIdAttribute(command));
            });
        }
        var ccs = child.getFirstChild("commands-from-client");
        if (ccs != null) {
            ccs.getChildren().forEach(command -> {
                //dd.getCommandsFromClient().add(CommonParserUtils.getIdAttribute(command));
            });
        }
    }

    private WebAppEntity getElementExtension(XmlNode child) {
        if (child == null) {
            return null;
        }
        var result = new WebAppEntity();
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
