/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.custom.JavaCustomCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.l10n.JavaL10nCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.remoting.JavaRemotingCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.remoting.WebRemotingCodeGenRecord;

import java.io.File;
import java.util.List;

public class ElsaWebCodeGenExtension {

    private final List<BaseCodeGenRecord> codegenRecords;

    private final File projectDir;

    public ElsaWebCodeGenExtension(List<BaseCodeGenRecord> codegenRecords, File projectDir){
        this.codegenRecords = codegenRecords;
        this.projectDir = projectDir;
    }

    public void remoting(String destDir, String typesDefinition, List<String> sourcesFileNames){
        var record = new WebRemotingCodeGenRecord();
        record.setTypesDefinition(typesDefinition);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

}
