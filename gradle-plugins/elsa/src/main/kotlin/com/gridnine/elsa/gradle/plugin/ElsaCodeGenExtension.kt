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

    fun folder(folder:String, configure: ElsaCodeFolderExtension.()->Unit){
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
        ElsaCodeFolderExtension(project, fd).configure()
    }

//    fun l10n(destDir: String, configurator: String, factory:String, sourcesFileNames: List<String>) {
//        val record = ElsaJavaL10nCodeGenRecord()
//        record.registryConfigurator = configurator
//        sourcesFileNames.forEach {
//            record.sources.add(project.file(it))
//        }
//        record.destinationDir = project.file(destDir)
//        record.factory = factory
//        findItem().l10nCodeGenRecords.add(record)
//    }
//
//    fun domain(destDir: String, configurator: String, sourcesFileNames: List<String>) {
//        val record = ElsaJavaDomainCodeGenRecord()
//        record.registryConfigurator = configurator
//        sourcesFileNames.forEach {
//            record.sources.add(project.file(it))
//        }
//        record.destinationDir = project.file(destDir)
//        findItem().domainCodeGenRecords.add(record)
//    }
//
//    fun custom(destDir: String, configurator: String, sourcesFileNames: List<String>) {
//        val record = ElsaJavaCustomCodeGenRecord()
//        record.registryConfigurator = configurator
//        sourcesFileNames.forEach {
//            record.sources.add(project.file(it))
//        }
//        record.destinationDir = project.file(destDir)
//        findItem().customCodeGenRecords.add(record)
//    }


}
