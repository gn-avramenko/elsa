/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen;

import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGen;
import com.gridnine.elsa.gradle.codegen.l10n.JavaL10nFactoryGenerator;
import com.gridnine.elsa.gradle.codegen.remoting.RemotingJavaCodeGen;
import com.gridnine.elsa.gradle.codegen.remoting.RemotingJavaMetaRegistryConfiguratorCodeGenerator;
import com.gridnine.elsa.gradle.codegen.remoting.RemotingTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.remoting.RemotingXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.serializable.SerializableTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.custom.CustomTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.custom.CustomXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.custom.JavaCustomMetaRegistryConfiguratorCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.DomainTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.domain.DomainXsdCodeGen;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainMetaRegistryConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.l10n.JavaL10nMetaRegistryConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.l10n.L10nTypesConfiguratorCodeGen;
import com.gridnine.elsa.gradle.codegen.l10n.L10nXsdCodeGen;
import com.gridnine.elsa.gradle.config.ElsaCodeGenProjectData;
import com.gridnine.elsa.gradle.config.ElsaJavaCustomCodeGenRecord;
import com.gridnine.elsa.gradle.config.ElsaJavaDomainCodeGenRecord;
import com.gridnine.elsa.gradle.config.ElsaJavaL10nCodeGenRecord;
import com.gridnine.elsa.gradle.config.ElsaJavaRemotingCodeGenRecord;
import com.gridnine.elsa.gradle.parser.custom.CustomMetadataParser;
import com.gridnine.elsa.gradle.parser.custom.CustomTypesParser;
import com.gridnine.elsa.gradle.parser.domain.DomainMetadataParser;
import com.gridnine.elsa.gradle.parser.domain.DomainTypesParser;
import com.gridnine.elsa.gradle.parser.l10n.L10nMetadataParser;
import com.gridnine.elsa.gradle.parser.l10n.L10nTypesParser;
import com.gridnine.elsa.gradle.parser.remoting.RemotingMetadataParser;
import com.gridnine.elsa.gradle.parser.remoting.RemotingTypesParser;
import com.gridnine.elsa.gradle.parser.serializable.SerializableTypesParser;
import com.gridnine.elsa.gradle.plugin.ElsaJavaExtension;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ElsaCodeGenTask extends DefaultTask {
    public ElsaCodeGenTask() {
        setGroup("elsa");
    }

    @TaskAction
    public void generate() throws Exception {
        if (getProject().equals(getProject().getRootProject())) {
            var javaExt = getProject().getExtensions().findByType(ElsaJavaExtension.class).getCodeGenExtension();
            var stp = new SerializableTypesParser();
            var stg = new SerializableTypesConfiguratorCodeGen();
            var dtp = new DomainTypesParser();
            var dtg = new DomainTypesConfiguratorCodeGen();
            var ctp = new CustomTypesParser();
            var ctg = new CustomTypesConfiguratorCodeGen();
            var rtp = new RemotingTypesParser();
            var rtg = new RemotingTypesConfiguratorCodeGen();
            var l10ntp = new L10nTypesParser();
            var l10ntg = new L10nTypesConfiguratorCodeGen();
            var totalDomainTypesRegistry = new DomainTypesRegistry();
            var totalCustomTypesRegistry = new CustomTypesRegistry();
            var totalSerializableTypesRegistry = new SerializableTypesRegistry();
            var totalL10nTypesRegistry = new L10nTypesRegistry();
            var totalSerializableMetaRegistry = new SerializableMetaRegistry();
            var remotingMetaRegistry = new RemotingMetaRegistry();
            var totalRemotingTypesRegistry = new RemotingTypesRegistry();
            var dmp = new DomainMetadataParser();
            var cmp = new CustomMetadataParser();
            var l10nmp = new L10nMetadataParser();
            var rmp = new RemotingMetadataParser();
            var cmg = new JavaCustomMetaRegistryConfiguratorCodeGenerator();
            var dmg = new JavaDomainMetaRegistryConfiguratorCodeGen();
            var rmg = new RemotingJavaMetaRegistryConfiguratorCodeGenerator();
            var l10nmg = new JavaL10nMetaRegistryConfiguratorCodeGen();
            var l10nfg = new JavaL10nFactoryGenerator();
            var dcg = new JavaDomainCodeGen();
            var rcd = new RemotingJavaCodeGen();
            var projects = new ArrayList<>(javaExt.getData().items);
            projects.sort((o1, o2) -> isDepends(o1.project, o2.project) ? 1 : (isDepends(o2.project, o1.project) ? -1 : 0));
            for (var projectData : projects) {
                for (var folderData : projectData.folders) {
                    Set<File> files = new LinkedHashSet<>();
                    for (var record : folderData.serializableTypes) {
                        var registry = new SerializableTypesRegistry();
                        for (File file : record.metadataFiles) {
                            stp.updateRegistry(registry, file);
                            stp.updateRegistry(totalSerializableTypesRegistry, file);
                        }
                        stg.generate(registry, folderData.folder, folderData.serializableTypesConfigurator, files);
                    }
                    for (var record : folderData.domainTypes) {
                        var registry = new DomainTypesRegistry();
                        for (File file : record.metadataFiles) {
                            dtp.updateRegistry(registry, file);
                            dtp.updateRegistry(totalDomainTypesRegistry, file);
                        }
                        dtg.generate(registry, folderData.folder, folderData.domainTypesConfigurator, files);
                    }
                    for (var record : folderData.customTypes) {
                        var registry = new CustomTypesRegistry();
                        for (File file : record.metadataFiles) {
                            ctp.updateRegistry(registry, file);
                            ctp.updateRegistry(totalCustomTypesRegistry, file);
                        }
                        ctg.generate(registry, folderData.folder, folderData.customTypesConfigurator, files);
                    }
                    for (var record : folderData.l10nTypes) {
                        var registry = new L10nTypesRegistry();
                        for (File file : record.metadataFiles) {
                            l10ntp.updateRegistry(registry, file);
                            l10ntp.updateRegistry(totalL10nTypesRegistry, file);
                        }
                        l10ntg.generate(registry, folderData.folder, folderData.l10nTypesConfigurator, files);
                    }
                    for (var record : folderData.remotingTypes) {
                        var registry = new RemotingTypesRegistry();
                        for (File file : record.metadataFiles) {
                            rtp.updateRegistry(registry, file);
                            rtp.updateRegistry(totalRemotingTypesRegistry, file);
                        }
                        rtg.generate(registry, folderData.folder, folderData.remotingTypesConfigurator, files);
                    }
                    if (folderData.customMetaRegistryConfigurator != null) {
                        var registry = new CustomMetaRegistry();
                        for (ElsaJavaCustomCodeGenRecord record : folderData.customCodeGenRecords) {
                            cmp.updateRegistry(registry, totalSerializableMetaRegistry, record.getSources());
                        }
                        cmg.generate(registry, totalSerializableMetaRegistry, folderData.customMetaRegistryConfigurator, folderData.folder, files);
                    }
                    if (folderData.l10nMetaRegistryConfigurator != null) {
                        var registry = new L10nMetaRegistry();
                        for (ElsaJavaL10nCodeGenRecord record : folderData.l10nCodeGenRecords) {
                            var reg2 = new L10nMetaRegistry();
                            l10nmp.updateMetaRegistry(registry, record.getSources());
                            l10nmp.updateMetaRegistry(reg2, record.getSources());
                            l10nfg.generate(record.getFactory(), reg2, totalSerializableTypesRegistry, totalL10nTypesRegistry, folderData.folder, files);
                        }
                        l10nmg.generate(registry, folderData.l10nMetaRegistryConfigurator, folderData.folder, files);
                    }
                    if (folderData.domainMetaRegistryConfigurator != null) {
                        var registry = new DomainMetaRegistry();
                        for (ElsaJavaDomainCodeGenRecord record : folderData.domainCodeGenRecords) {
                            dmp.updateRegistry(registry, totalSerializableMetaRegistry, record.getSources());
                        }
                        dmg.generate(registry, totalSerializableMetaRegistry, folderData.domainMetaRegistryConfigurator, folderData.folder, files);
                        dcg.generate(registry, totalSerializableMetaRegistry, totalSerializableTypesRegistry, totalDomainTypesRegistry, folderData.folder, files);
                    }
                    if (folderData.remotingMetaRegistryConfigurator != null) {
                        var registry = new RemotingMetaRegistry();
                        for (ElsaJavaRemotingCodeGenRecord record : folderData.remotingCodeGenRecords) {
                            rmp.updateRegistry(registry, totalSerializableMetaRegistry, record.getSources());
                        }
                        rcd.generate(registry, totalSerializableMetaRegistry, totalSerializableTypesRegistry, totalRemotingTypesRegistry, folderData.folder, files);
                        rmg.generate(registry, totalSerializableMetaRegistry, folderData.remotingMetaRegistryConfigurator, folderData.folder, files);
                    }
                    cleanupDir(folderData.folder, files);
                }
            }
            if (javaExt.getData().xsdsLocation != null) {
                new DomainXsdCodeGen().generate(totalDomainTypesRegistry, totalSerializableTypesRegistry, javaExt.getData().xsdsLocation, javaExt.getData().xsdsCustomizationSuffix);
                new CustomXsdCodeGen().generate(totalCustomTypesRegistry, totalSerializableTypesRegistry, javaExt.getData().xsdsLocation, javaExt.getData().xsdsCustomizationSuffix);
                new L10nXsdCodeGen().generate(totalL10nTypesRegistry, totalSerializableTypesRegistry, javaExt.getData().xsdsLocation, javaExt.getData().xsdsCustomizationSuffix);
                new RemotingXsdCodeGen().generate(totalRemotingTypesRegistry, totalSerializableTypesRegistry, javaExt.getData().xsdsLocation, javaExt.getData().xsdsCustomizationSuffix);
            }
        }
    }

    private boolean isDepends(Project proj1, Project proj2) {
        return proj1.getConfigurations().getByName("implementation").getAllDependencies().withType(ProjectDependency.class).stream()
                .anyMatch(it -> {
                    if (it instanceof DefaultProjectDependency dpg) {
                        return dpg.getDependencyProject().getName().equals(proj2.getName());
                    }
                    return false;
                });
    }

    private void cleanupDir(File dir, Set<File> generatedFiles) {
        var files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                cleanupDir(file, generatedFiles);
            }
            if (!generatedFiles.contains(file)) {
                file.delete();
            }
        }
        if (Objects.requireNonNull(dir.listFiles()).length == 0) {
            dir.delete();
        }
    }
}
