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

package com.gridnine.platform.elsa.gradle.meta.adminUi.form;

import com.gridnine.platform.elsa.gradle.meta.adminUi.AdminUiMetaRegistry;
import com.gridnine.platform.elsa.gradle.meta.adminUi.common.AdminUiParserHelper;
import com.gridnine.platform.elsa.gradle.meta.common.XmlNode;
import com.gridnine.platform.elsa.gradle.parser.common.CommonParserUtils;

public class FormMetadataParser {
    public static FormContainerDescription parseForm(XmlNode child, boolean root, AdminUiMetaRegistry registry){
        var formDescription = new FormContainerDescription();
        if(root){
            formDescription.setClassName(AdminUiParserHelper.getClassName(child));
            registry.getContainers().put(formDescription.getClassName(), formDescription);
        }
        child.getChildren().forEach(childNode -> {
            var tag = childNode.getName();
            if("text-field".equals(tag)){
                var textFieldDescription = new FormTextFieldDescription();
                textFieldDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, textFieldDescription.getTitle());
                formDescription.getComponents().put(textFieldDescription.getId(), textFieldDescription);
                return;
            }
            if("text-area".equals(tag)){
                var textAreaDescription = new FormTextAreaDescription();
                textAreaDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, textAreaDescription.getTitle());
                formDescription.getComponents().put(textAreaDescription.getId(), textAreaDescription);
                return;
            }
            if("boolean-field".equals(tag)){
                var booleanFieldDescription = new FormBooleanFieldDescription();
                booleanFieldDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, booleanFieldDescription.getTitle());
                formDescription.getComponents().put(booleanFieldDescription.getId(), booleanFieldDescription);
                return;
            }
            if("date-interval-field".equals(tag)){
                var dateIntervalFieldDescription = new FormDateIntervalFieldDescription();
                dateIntervalFieldDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, dateIntervalFieldDescription.getTitle());
                formDescription.getComponents().put(dateIntervalFieldDescription.getId(), dateIntervalFieldDescription);
                return;
            }
            if("custom".equals(tag)){
                var customFieldDescription = new FormCustomElementDescription();
                customFieldDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                customFieldDescription.setClassName(childNode.getAttribute("class-name"));
                AdminUiParserHelper.updateTitle(childNode, customFieldDescription.getTitle());
                formDescription.getComponents().put(customFieldDescription.getId(), customFieldDescription);
                return;
            }
            if("remote-select".equals(tag)){
                var remoteSelectDescription = new FormRemoteSelectDescription();
                remoteSelectDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, remoteSelectDescription.getTitle());
                formDescription.getComponents().put(remoteSelectDescription.getId(), remoteSelectDescription);
                return;
            }
            if("select".equals(tag)){
                var selectDescription = new FormSelectDescription();
                selectDescription.setId(CommonParserUtils.getIdAttribute(childNode));
                AdminUiParserHelper.updateTitle(childNode, selectDescription.getTitle());
                formDescription.getComponents().put(selectDescription.getId(), selectDescription);
                return;
            }
        });
        return  formDescription;
    }
}
