package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsExtensionData
import com.gridnine.elsa.gradle.config.ElsaCodeGenTsFolderData
import com.gridnine.elsa.gradle.config.ElsaCodeGenTsProjectData
import org.gradle.api.Project

@ElsaTsConfigMarker
open class ElsaCodeGenTsExtension(private val project: Project, val data: ElsaCodeGenTsExtensionData) {

    private fun getOrCreateProject():ElsaCodeGenTsProjectData{
        var item = data.items.find { it.project == project}
        if (item == null) {
            item = ElsaCodeGenTsProjectData()
            item.project = project
            data.items.add(item)
        }
        return item;
    }
    fun packageName(name: String){
        getOrCreateProject().packageName = name
    }

    fun priority(value: Double){
        getOrCreateProject().priority = value
    }

    fun folder(folder:String,  configure: ElsaCodeGenTsFolderExtension.()->Unit){
        folder(folder, false, configure);
    }
    fun folder(folder:String, dontCleanup:Boolean, configure: ElsaCodeGenTsFolderExtension.()->Unit){
        var item = getOrCreateProject()
        val dir = project.file(folder);
        var fd = item.folders.find { it.folder == dir }
        if(fd == null){
            fd = ElsaCodeGenTsFolderData()
            fd.folder = dir
            fd.dontCleanup = dontCleanup;
            item.folders.add(fd)
        }
        ElsaCodeGenTsFolderExtension(project, fd).configure()
    }

}
