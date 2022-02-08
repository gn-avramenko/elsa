/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGenRecord;

import java.io.File;
import java.util.List;

public class ElsaJavaCodeGenExtension {

    private String artefactId;

    public String getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(String artefactId) {
        this.artefactId = artefactId;
    }

    private final List<BaseCodeGenRecord> codegenRecords;

    public ElsaJavaCodeGenExtension(List<BaseCodeGenRecord> codegenRecords){
        this.codegenRecords = codegenRecords;
    }

    public void domain(String sourceFileName, String destDir, String configurator){
        var record = new JavaDomainCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        record.setSource(new File(sourceFileName));
        record.setDestinationDir(new File(destDir));
        codegenRecords.add(record);
    }
}
