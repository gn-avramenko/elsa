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
import com.gridnine.elsa.gradle.codegen.ui.template.JavaUiTemplateCodeGenRecord;

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

    public void domain(String destDir, String configurator, List<String> sourcesFileNames){
        var record = new JavaDomainCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void custom(String destDir, String configurator, List<String> sourcesFileNames){
        var record = new JavaCustomCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void remoting(String destDir, String configurator, List<String> sourcesFileNames){
        var record = new JavaRemotingCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void l10n( String destDir, String configurator, String factory, List<String> sourcesFileNames){
        var record = new JavaL10nCodeGenRecord();
        record.setRegistryConfigurator(configurator);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setFactory(factory);
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

    public void uiTemplate( String destDir, String xsdFileName, String targetNameSpace, List<String> sourcesFileNames){
        var record = new JavaUiTemplateCodeGenRecord();
        record.setDestinationDir(new File(projectDir, destDir));
        record.setTargetNameSpace(targetNameSpace);
        sourcesFileNames.forEach(it -> record.getSources().add(new File(projectDir, it)));
        record.setXsdFileName(xsdFileName);
        record.setDestinationDir(new File(projectDir, destDir));
        codegenRecords.add(record);
    }

}
