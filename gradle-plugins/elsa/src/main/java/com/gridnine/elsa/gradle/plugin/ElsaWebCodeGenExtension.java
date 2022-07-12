/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.remoting.WebRemotingCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.ui.WebUiCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.ui.template.WebUiTemplateCodeGenRecord;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElsaWebCodeGenExtension {

    private final List<BaseCodeGenRecord> codegenRecords;

    private final Map<String, File> imports;

    private final File projectDir;

    public ElsaWebCodeGenExtension(List<BaseCodeGenRecord> codegenRecords, File projectDir, Map<String,File> imports) {
        this.codegenRecords = codegenRecords;
        this.projectDir = projectDir;
        this.imports = imports;
    }

    public void remoting(String destDir, String typesDefinition, List<String> sourcesFileNames){
        var record = new WebRemotingCodeGenRecord();
        record.setTypesDefinition(typesDefinition);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void uiTemplate(String destDir, String tsFileName, List<String> sourcesFileNames){
        var record = new WebUiTemplateCodeGenRecord();
        record.setTsFileName(tsFileName);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void ui(String destDir, String tsFileName, List<String> sourcesFileNames){
        var record = new WebUiCodeGenRecord();
        record.setTsFileName(tsFileName);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void declareImport(String path, String... widgets){
        for(String widget: widgets){
            imports.put(widget, new File(projectDir, path));
        }
    }
}
