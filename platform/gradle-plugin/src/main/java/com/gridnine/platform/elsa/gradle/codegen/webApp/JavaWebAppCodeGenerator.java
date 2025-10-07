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

package com.gridnine.platform.elsa.gradle.codegen.webApp;

import com.gridnine.platform.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.codegen.webApp.helpers.JavaWebAppElementsHelper;
import com.gridnine.platform.elsa.gradle.codegen.webApp.helpers.JavaWebAppEntityHelper;
import com.gridnine.platform.elsa.gradle.meta.common.EntityDescription;
import com.gridnine.platform.elsa.gradle.meta.common.EnumDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardPropertyDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.webApp.CustomWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebAppMetaRegistry;
import com.gridnine.platform.elsa.gradle.parser.webApp.WebAppMetaRegistryParser;
import com.gridnine.platform.elsa.gradle.parser.webApp.WebAppMetadataHelper;

import java.io.File;
import java.util.*;

public class JavaWebAppCodeGenerator implements CodeGenerator<JavaWebAppCodeGenRecord> {

    @Override
    public void generate(JavaWebAppCodeGenRecord record, File destDir, Set<File> generatedFiles, Map<Object, Object> context) throws Exception {
        var parser = new WebAppMetaRegistryParser();
        var coll = new ArrayList<>(record.getSources());
        coll.addAll(record.getExternalInjections());
        var metaRegistry = new WebAppMetaRegistry();
        parser.updateMetaRegistry(metaRegistry, coll);
        for (EnumDescription ed : metaRegistry.getEnums().values()) {
            JavaCodeGeneratorUtils.generateJavaEnumCode(ed, destDir, generatedFiles);
        }
        for (EntityDescription ed : metaRegistry.getEntities().values()) {
            JavaWebAppEntityHelper.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
        for(var elm: metaRegistry.getElements().values()){
            var ed = WebAppMetadataHelper.getConfigurationDescription(elm);
            JavaWebAppEntityHelper.generateJavaEntityCode(ed, destDir, generatedFiles);
            CustomWebElementDescription ce = WebAppMetadataHelper.toCustomEntity(elm);
            if(ce.getInput() != null){
                var id = WebAppMetadataHelper.getInputValueDescription(ce);
                JavaWebAppEntityHelper.generateJavaEntityCode(id, destDir, generatedFiles);
            }
        }
        JavaWebAppElementsHelper.generate(metaRegistry, destDir, record.getSourceDir(), generatedFiles);
    }
}
