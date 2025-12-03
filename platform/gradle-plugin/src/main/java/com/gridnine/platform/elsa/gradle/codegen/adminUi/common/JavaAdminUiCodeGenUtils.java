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

package com.gridnine.platform.elsa.gradle.codegen.adminUi.common;

import com.gridnine.platform.elsa.gradle.codegen.adminUi.form.JavaAdminUiFormConfiguratorHelper;
import com.gridnine.platform.elsa.gradle.codegen.adminUi.grid.JavaAdminUiGridConfiguratorHelper;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.meta.adminUi.BaseAdminUiContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.grid.GridContainerDescription;

import java.util.Locale;
import java.util.Map;

public class JavaAdminUiCodeGenUtils {
    public static void updateTitle(Map<Locale, String> map, JavaCodeGenerator generator) {
        for (Map.Entry<Locale, String> entry : map.entrySet()) {
            generator.addImport("com.gridnine.platform.elsa.common.core.utils.LocaleUtils");
            generator.printLine("comp.getTitle().put(LocaleUtils.getLocale(\"%s\", null), \"%s\");".formatted(entry.getKey().getLanguage(), entry.getValue()));
        }
    }

    public static void generateDescription(BaseAdminUiContainerDescription cd, String containerDescriptionName, boolean root, JavaCodeGenerator gen) throws Exception {
        switch (cd.getType()) {
            case FORM -> {
                JavaAdminUiFormConfiguratorHelper.generateDescription((FormContainerDescription) cd, containerDescriptionName, root, gen);
            }
            case GRID -> {
                JavaAdminUiGridConfiguratorHelper.generateDescription((GridContainerDescription) cd, containerDescriptionName, root, gen);
            }
        }
    }
}