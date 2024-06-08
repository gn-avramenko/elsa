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

package com.gridnine.platform.elsa.gradle.codegen.custom;

import com.gridnine.platform.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.platform.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.platform.elsa.gradle.parser.custom.CustomMetaRegistryParser;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class JavaCustomCodeGenerator implements CodeGenerator<JavaCustomCodeGenRecord> {
    @Override
    public void generate(JavaCustomCodeGenRecord record, File destDir, Set<File> generatedFiles, Map<Object, Object> context) {
        var parser = new CustomMetaRegistryParser();
        var metaRegistry = new CustomMetaRegistry();
        parser.updateMetaRegistry(metaRegistry, record.getSources());
        BuildExceptionUtils.wrapException(() -> JavaCustomConfiguratorCodeGenerator.generate(metaRegistry, record.getRegistryConfigurator(), destDir, generatedFiles));
    }
}
