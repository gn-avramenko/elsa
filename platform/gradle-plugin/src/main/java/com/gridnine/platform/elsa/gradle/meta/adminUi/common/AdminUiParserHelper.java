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

package com.gridnine.platform.elsa.gradle.meta.adminUi.common;

import com.gridnine.platform.elsa.gradle.meta.adminUi.AdminUiMetaRegistry;
import com.gridnine.platform.elsa.gradle.meta.adminUi.BaseAdminUiContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormMetadataParser;
import com.gridnine.platform.elsa.gradle.meta.adminUi.grid.GridMetadataParser;
import com.gridnine.platform.elsa.gradle.meta.common.XmlNode;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.util.Locale;
import java.util.Map;

public class AdminUiParserHelper {

    private static Locale ruLocale = Locale.forLanguageTag("ru");
    public static String getClassName(XmlNode node) {
        String className = node.getAttribute("class-name");
        if(className == null){
            throw new RuntimeException("class name is null");
        }
        return className;
    }

    public static BaseAdminUiContainerDescription parse(XmlNode node, boolean root, AdminUiMetaRegistry registry) {
        if("form".equals(node.getName())){
            return FormMetadataParser.parseForm(node, root, registry);
        }
        if("grid".equals(node.getName())){
            return GridMetadataParser.parseForm(node, root, registry);
        }
        throw new RuntimeException("unsupported tag name " + node.getName());
    }

    public static void updateTitle(XmlNode node, Map<Locale, String> titleMap) {
        String title = node.getAttribute("title");
        if(BuildTextUtils.isBlank(title)){
            return;
        }
        for(var item: title.split("\\|")){
            var t = item.trim();
            if(BuildTextUtils.isBlank(item)){
                continue;
            }
            if(t.startsWith("en:")){
                titleMap.put(Locale.ENGLISH, t.substring(3));
            } else if (t.startsWith("ru:")){
                titleMap.put(ruLocale, t.substring(3));
            } else {
                titleMap.put(Locale.ENGLISH, t);
            }
        }
    }
}
