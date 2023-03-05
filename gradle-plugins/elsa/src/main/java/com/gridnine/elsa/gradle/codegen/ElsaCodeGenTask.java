/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen;

import com.gridnine.elsa.gradle.codegen.types.CustomTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.types.CustomXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.types.DomainTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.types.DomainXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.types.L10nTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.types.L10nXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.types.SerializableTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.config.ElsaTypesFileData;
import com.gridnine.elsa.gradle.config.ElsaTypesProjectData;
import com.gridnine.elsa.gradle.parser.CustomTypesParser;
import com.gridnine.elsa.gradle.parser.DomainTypesParser;
import com.gridnine.elsa.gradle.parser.L10nTypesParser;
import com.gridnine.elsa.gradle.parser.SerializableTypesParser;
import com.gridnine.elsa.gradle.plugin.ElsaJavaExtension;
import com.gridnine.elsa.gradle.plugin.ElsaTypesExtension;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ElsaCodeGenTask extends DefaultTask {
    public ElsaCodeGenTask() {
        setGroup("elsa");
    }

    @TaskAction
    public void generate() throws Exception {
        if(getProject().equals(getProject().getRootProject())){
            ElsaTypesExtension ext = getProject().getExtensions().findByType(ElsaJavaExtension.class).getTypesExtension();
            Map<File, Set<File>> generatedFiles = new LinkedHashMap<>();
            var stp = new SerializableTypesParser();
            var stg = new SerializableTypesConfiguratorCodeGen();
            var dtp = new DomainTypesParser();
            var dtg = new DomainTypesConfiguratorCodeGen();
            var ctp = new CustomTypesParser();
            var ctg = new CustomTypesConfiguratorCodeGen();
            var l10ntp = new L10nTypesParser();
            var l10ntg = new L10nTypesConfiguratorCodeGen();
            var totalDomainRegistry = new DomainTypesRegistry();
            var totalCustomRegistry = new CustomTypesRegistry();
            var totalSerializableTypesRegistry = new SerializableTypesRegistry();
            var totalL10nTypesRegistry = new L10nTypesRegistry();
            for(ElsaTypesProjectData projectData : ext.getData().items){
                var files = generatedFiles.computeIfAbsent(projectData.destDir, k -> new HashSet<>());
                for(ElsaTypesFileData fileData: projectData.serializableTypes){
                    var registry = new SerializableTypesRegistry();
                    stp.updateRegistry(registry, fileData.metadataFile);
                    stp.updateRegistry(totalSerializableTypesRegistry, fileData.metadataFile);
                    stg.generate(registry, projectData.destDir, fileData.configuratorClassName, files);
                }
                for(ElsaTypesFileData fileData: projectData.domainTypes){
                    var registry = new DomainTypesRegistry();
                    dtp.updateRegistry(registry, fileData.metadataFile);
                    dtp.updateRegistry(totalDomainRegistry, fileData.metadataFile);
                    dtg.generate(registry, projectData.destDir, fileData.configuratorClassName, files);
                }
                for(ElsaTypesFileData fileData: projectData.customTypes){
                    var registry = new CustomTypesRegistry();
                    ctp.updateRegistry(registry, fileData.metadataFile);
                    ctp.updateRegistry(totalCustomRegistry, fileData.metadataFile);
                    ctg.generate(registry, projectData.destDir, fileData.configuratorClassName, files);
                }for(ElsaTypesFileData fileData: projectData.l10nTypes){
                    var registry = new L10nTypesRegistry();
                    l10ntp.updateRegistry(registry, fileData.metadataFile);
                    l10ntp.updateRegistry(totalL10nTypesRegistry, fileData.metadataFile);
                    l10ntg.generate(registry, projectData.destDir, fileData.configuratorClassName, files);
                }

            }
            for(Map.Entry<File, Set<File>> entry: generatedFiles.entrySet()){
                cleanupDir(entry.getKey(), entry.getValue());
            }
            new DomainXsdCodeGen().generate(totalDomainRegistry,totalSerializableTypesRegistry, ext.getData().xsdsLocation, ext.getData().xsdsCustomizationSuffix);
            new CustomXsdCodeGen().generate(totalCustomRegistry,totalSerializableTypesRegistry, ext.getData().xsdsLocation, ext.getData().xsdsCustomizationSuffix);
            new L10nXsdCodeGen().generate(totalL10nTypesRegistry,totalSerializableTypesRegistry, ext.getData().xsdsLocation, ext.getData().xsdsCustomizationSuffix);
        }
    }
    private  void cleanupDir(File dir, Set<File> generatedFiles) {
        for(File file: Objects.requireNonNull(dir.listFiles())){
            if(file.isDirectory()){
                cleanupDir(file, generatedFiles);
            }
            if(!generatedFiles.contains(file)){
                file.delete();
            }
        }
        if(Objects.requireNonNull(dir.listFiles()).length == 0){
            dir.delete();
        }
    }
}
