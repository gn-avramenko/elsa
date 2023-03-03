/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen;

import com.gridnine.elsa.gradle.codegen.types.SerializableTypeConfiguratorCodeGen;
import com.gridnine.elsa.gradle.config.ElsaTypesFileData;
import com.gridnine.elsa.gradle.config.ElsaTypesProjectData;
import com.gridnine.elsa.gradle.parser.SerializableTypesParser;
import com.gridnine.elsa.gradle.plugin.ElsaTypesExtension;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ElsaCodeGenTask extends DefaultTask {
    public ElsaCodeGenTask() {
        setGroup("elsa");
    }

    @TaskAction
    public void generate() throws Exception {
        if(getProject().equals(getProject().getRootProject())){
            ElsaTypesExtension ext = getProject().getExtensions().findByType(ElsaTypesExtension.class);
            Map<File, Set<File>> generatedFiles = new LinkedHashMap<>();
            var stp = new SerializableTypesParser();
            var stg = new SerializableTypeConfiguratorCodeGen();
            assert ext != null;
            for(ElsaTypesProjectData projectData : ext.getData().items){
                var files = generatedFiles.computeIfAbsent(projectData.destDir, k -> new HashSet<>());
                for(ElsaTypesFileData fileData: projectData.serializableTypes){
                    var registry = new SerializableTypesRegistry();
                    stp.updateRegistry(registry, fileData.metadataFile);
                    stg.generate(registry, projectData.destDir, fileData.configuratorClassName, files);
                }
            }
        }
    }
}
