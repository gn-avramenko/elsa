/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.custom.JavaCustomCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.rest.JavaRestCodeGenRecord;

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

    private final File projectDir;

    public ElsaJavaCodeGenExtension(List<BaseCodeGenRecord> codegenRecords, File projectDir){
        this.codegenRecords = codegenRecords;
        this.projectDir = projectDir;
    }

    public void domain(String sourceFileName, String destDir, String configurator){
        var record = new JavaDomainCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        record.setSource(new File(projectDir, sourceFileName));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void rest(String sourceFileName, String destDir, String configurator){
        var record = new JavaRestCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        record.setSource(new File(projectDir, sourceFileName));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void custom(String sourceFileName, String destDir, String configurator){
        var record = new JavaCustomCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        record.setSource(new File(projectDir, sourceFileName));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }
}
