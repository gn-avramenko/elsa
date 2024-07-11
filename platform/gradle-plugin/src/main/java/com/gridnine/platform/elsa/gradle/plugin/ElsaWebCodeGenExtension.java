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

package com.gridnine.platform.elsa.gradle.plugin;

import com.gridnine.platform.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.platform.elsa.gradle.codegen.l10n.WebL10nCodeGenRecord;
import com.gridnine.platform.elsa.gradle.codegen.remoting.WebRemotingCodeGenRecord;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ElsaWebCodeGenExtension {

    private final List<BaseCodeGenRecord> codegenRecords;

    private final File projectDir;

    public ElsaWebCodeGenExtension(List<BaseCodeGenRecord> codegenRecords, File projectDir) {
        this.codegenRecords = codegenRecords;
        this.projectDir = projectDir;
    }

    public void remoting(String destDir,  List<String> sourcesFileNames) {
        var record = new WebRemotingCodeGenRecord();
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void l10n(String destDir, String l10nFileName, String tsClassName, List<String> sourcesFileNames) {
        var record = new WebL10nCodeGenRecord();
        record.setTsClassName(tsClassName);
        record.setL10nFileName(l10nFileName);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }
}
