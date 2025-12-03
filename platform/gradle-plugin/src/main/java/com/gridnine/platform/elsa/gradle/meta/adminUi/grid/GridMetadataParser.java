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

package com.gridnine.platform.elsa.gradle.meta.adminUi.grid;

import com.gridnine.platform.elsa.gradle.meta.adminUi.AdminUiMetaRegistry;
import com.gridnine.platform.elsa.gradle.meta.adminUi.common.AdminUiParserHelper;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.*;
import com.gridnine.platform.elsa.gradle.meta.common.XmlNode;
import com.gridnine.platform.elsa.gradle.parser.common.CommonParserUtils;

public class GridMetadataParser {
    public static GridContainerDescription parseForm(XmlNode child, boolean root, AdminUiMetaRegistry registry){
        var gridContainerDescription = new GridContainerDescription();
        if(root){
            gridContainerDescription.setClassName(AdminUiParserHelper.getClassName(child));
            registry.getContainers().put(gridContainerDescription.getClassName(), gridContainerDescription);
        }
        child.getChildren().forEach(rowNode -> {
            var rowDescription = new GridRowDescription();
            gridContainerDescription.getRows().add(rowDescription);
            rowNode.getChildren().forEach(columnNode -> {
               var columnDescription = new GridColumnDescription();
               rowDescription.getColumns().add(columnDescription);
               columnDescription.setSmallWidth(getWidth(columnNode, "small-width"));
               columnDescription.setStandardWidth(getWidth(columnNode, "standard-width"));
               columnDescription.setLargeWidth(getWidth(columnNode, "large-width"));
               var contentNode = columnNode.getChildren().get(0);
               columnDescription.setContent(AdminUiParserHelper.parse(contentNode, false, registry));
            });
        });
        return gridContainerDescription;
    }

    private static int getWidth(XmlNode columnNode, String attrName) {
        var value = columnNode.getAttribute(attrName);
        return value != null ? Integer.parseInt(value) : -1;
    }
}
