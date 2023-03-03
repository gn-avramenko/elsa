/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.config.ElsaTypesExtensionData;
import com.gridnine.elsa.gradle.config.ElsaTypesFileData;
import com.gridnine.elsa.gradle.config.ElsaTypesProjectData;
import org.gradle.api.Project;

public class ElsaTypesExtension {

    private final Project project;

    private final ElsaTypesExtensionData data;

    public ElsaTypesExtension(Project project, ElsaTypesExtensionData data ) {
        this.project = project;
        this.data = data;
    }

    private ElsaTypesProjectData findItem() {
        var item = data.items.stream().filter(it -> it.project.equals(project)).findFirst().orElse(null);
        if(item == null){
            item = new ElsaTypesProjectData();
            item.project = project;
            data.items.add(item);
        }
        return item;

    }
    public void destDir(String dir){
        findItem().destDir = project.file(dir);

    }
    public void serialization(String path, String configuratorClassName){
        findItem().serializableTypes.add(new ElsaTypesFileData(project.file(path), configuratorClassName));
    }

    public void domain(String path, String configuratorClassName){
        findItem().domainTypes.add(new ElsaTypesFileData(project.file(path),configuratorClassName));
    }

    public void xsdsLocation(String path){
        data.xsdsLocation = project.file(path);
    }

    public ElsaTypesExtensionData getData() {
        return data;
    }
}
