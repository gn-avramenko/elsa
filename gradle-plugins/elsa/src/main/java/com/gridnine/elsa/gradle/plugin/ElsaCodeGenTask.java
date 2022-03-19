/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;
import com.gridnine.elsa.gradle.codegen.custom.JavaCustomCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGenerator;
import com.gridnine.elsa.gradle.codegen.l10n.JavaL10nCodeGenerator;
import com.gridnine.elsa.gradle.codegen.rest.JavaRestCodeGenerator;
import com.gridnine.elsa.gradle.parser.domain.DomainMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ElsaCodeGenTask extends DefaultTask {
    public ElsaCodeGenTask(){
        setGroup("elsa");
    }
    @TaskAction
    public void generate(){
        var records = getProject().getExtensions()
                .getByType(ElsaJavaExtension.class).getCodegenRecords();
        Set<File> generatedFiles = new HashSet<>();
        var generators = new LinkedHashMap<GeneratorType, Map<File, List<BaseCodeGenRecord>>>();
        records.forEach(it-> {
            var dests = generators
                    .computeIfAbsent(it.getGeneratorType(), t -> new LinkedHashMap<>());
            dests.computeIfAbsent(it.getDestinationDir(), t -> new ArrayList<>()).add(it);
        });
        Map<Object,Object> context = new HashMap<>();
        var domainMetaRegistry = new DomainMetaRegistry();
        context.put("domain-meta-registry", domainMetaRegistry);
        var parser = new DomainMetaRegistryParser();
        var projects = new ArrayList<Project>();
        getProject().getConfigurations().getAt("implementation").getAllDependencies().withType(ProjectDependency.class).forEach((proj) -> projects.add(proj.getDependencyProject()));
        projects.add(getProject());
        projects.forEach(project -> {
            try {
                var extension = project.getExtensions().getByName("elsa-java-configuration");
                var records2 = (List<?>) extension.getClass().getMethod("getCodegenRecords").invoke(extension);
                for(Object record : records2){
                    var type = (Enum<?>) record.getClass().getMethod("getGeneratorType").invoke(record);
                    if("JAVA_DOMAIN".equals(type.name())){
                        File source = (File) record.getClass().getMethod("getSource").invoke(record);
                        parser.updateMetaRegistry(domainMetaRegistry, Collections.singletonList(source));
                    }
                }
            } catch (Exception e){
                //noops
            }
        });
        generators.forEach((key, value) -> {
            @SuppressWarnings("unchecked") var codeGen = (CodeGenerator<BaseCodeGenRecord>) switch (key) {
                case JAVA_DOMAIN -> new JavaDomainCodeGenerator();
                case JAVA_REST -> new JavaRestCodeGenerator();
                case JAVA_CUSTOM -> new JavaCustomCodeGenerator();
                case JAVA_L10N -> new JavaL10nCodeGenerator();
            };
            value.forEach((key1, value1) -> BuildExceptionUtils.wrapException(() -> codeGen.generate(value1, key1, generatedFiles, context)));
        });
        Set<File> destDirs = new HashSet<>();
        records.forEach(it -> destDirs.add(it.getDestinationDir()));
        cleanupDirs(destDirs, generatedFiles);
    }

    private boolean cleanupDirs(Collection<File> destDirs, Set<File> generatedFiles) {
        var result = new AtomicBoolean(false);
        destDirs.forEach(fileOrDir ->{
            if(!fileOrDir.exists()){
                return;
            }
            if(fileOrDir.isFile()){
                if(!generatedFiles.contains(fileOrDir)){
                    assert fileOrDir.delete();
                } else {
                    result.set(true);
                }
                return;
            }
            var subRes = cleanupDirs(Arrays.asList(Objects.requireNonNull(fileOrDir.listFiles())),generatedFiles);
            if(subRes){
                result.set(true);
                return;
            }
            assert fileOrDir.delete();
        });
        return result.get();
    }
}
