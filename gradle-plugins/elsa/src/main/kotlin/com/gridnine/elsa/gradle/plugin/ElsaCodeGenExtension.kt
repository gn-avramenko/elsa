package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenExtensionData
import com.gridnine.elsa.gradle.config.ElsaCodeGenFolderData
import com.gridnine.elsa.gradle.config.ElsaCodeGenProjectData
import org.gradle.api.Project

@ElsaJavaConfigMarker
open class ElsaCodeGenExtension(private val project: Project, val data: ElsaCodeGenExtensionData) {

    fun xsdLocation(location:String){
        data.xsdsLocation = project.file(location)
    }

    fun xsdsCustomizationSuffix(suffix:String){
        data.xsdsCustomizationSuffix = suffix
    }

    fun folder(folder:String, configure: ElsaCodeGenFolderExtension.()->Unit){
        var item = data.items.stream().filter { it: ElsaCodeGenProjectData -> it.project == project }
            .findFirst().orElse(null)
        if (item == null) {
            item = ElsaCodeGenProjectData()
            item.project = project
            data.items.add(item)
        }
        val dir = project.file(folder);
        var fd = item.folders.find { it.folder == dir }
        if(fd == null){
            fd = ElsaCodeGenFolderData()
            fd.folder = dir
            item.folders.add(fd)
        }
        ElsaCodeGenFolderExtension(project, fd).configure()
    }

}
