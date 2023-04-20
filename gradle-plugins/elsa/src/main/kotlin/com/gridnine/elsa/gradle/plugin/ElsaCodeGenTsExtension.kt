package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsExtensionData
import com.gridnine.elsa.gradle.config.ElsaCodeGenTsFolderData
import com.gridnine.elsa.gradle.config.ElsaCodeGenTsProjectData
import org.gradle.api.Project

@ElsaTsConfigMarker
open class ElsaCodeGenTsExtension(private val project: Project, val data: ElsaCodeGenTsExtensionData) {

    fun packageName(name: String){
        var item = data.items.find { it.project == project}
        if (item == null) {
            item = ElsaCodeGenTsProjectData()
            item.project = project
            data.items.add(item)
        }
        item.packageName = name
    }

    fun folder(folder:String, configure: ElsaCodeGenTsFolderExtension.()->Unit){
        var item = data.items.find { it.project == project}
        if (item == null) {
            item = ElsaCodeGenTsProjectData()
            item.project = project
            data.items.add(item)
        }
        val dir = project.file(folder);
        var fd = item.folders.find { it.folder == dir }
        if(fd == null){
            fd = ElsaCodeGenTsFolderData()
            fd.folder = dir
            item.folders.add(fd)
        }
        ElsaCodeGenTsFolderExtension(project, fd).configure()
    }

}
