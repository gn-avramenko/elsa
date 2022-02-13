/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.common.BaseCodeGenRecord;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.GeneratorType;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.custom.JavaCustomCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCodeGenerator;
import com.gridnine.elsa.gradle.codegen.rest.JavaRestCodeGenerator;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildRunnableWithException;
import org.gradle.api.DefaultTask;
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
        generators.forEach((key, value) -> {
            @SuppressWarnings("unchecked") var codeGen = (CodeGenerator<BaseCodeGenRecord>) switch (key) {
                case JAVA_DOMAIN -> new JavaDomainCodeGenerator();
                case JAVA_REST -> new JavaRestCodeGenerator();
                case JAVA_CUSTOM -> new JavaCustomCodeGenerator();
            };
            value.forEach((key1, value1) -> BuildExceptionUtils.wrapException(() -> codeGen.generate(value1, key1, generatedFiles)));
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
