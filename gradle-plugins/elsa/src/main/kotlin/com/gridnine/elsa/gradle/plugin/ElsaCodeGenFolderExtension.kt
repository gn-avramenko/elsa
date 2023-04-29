package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenFolderData
import com.gridnine.elsa.gradle.config.ElsaJavaCustomCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaJavaDomainCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaJavaL10nCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaJavaRemotingCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTypesRecord
import org.gradle.api.Project

@ElsaJavaConfigMarker
open class ElsaCodeGenFolderExtension(private val project:Project, private val folderData:ElsaCodeGenFolderData) {

    fun serializableTypesConfigurator(configurator:String){
        folderData.serializableTypesConfigurator = configurator
    }
    fun domainTypesConfigurator(configurator:String){
        folderData.domainTypesConfigurator = configurator
    }
    fun l10nTypesConfigurator(configurator:String){
        folderData.l10nTypesConfigurator = configurator
    }
    fun customTypesConfigurator(configurator:String){
        folderData.customTypesConfigurator = configurator
    }
    fun domainMetaRegistryConfigurator(configurator:String){
        folderData.domainMetaRegistryConfigurator = configurator
    }
    fun customMetaRegistryConfigurator(configurator:String){
        folderData.customMetaRegistryConfigurator = configurator
    }
    fun l10nMetaRegistryConfigurator(configurator:String){
        folderData.l10nMetaRegistryConfigurator = configurator
    }

    fun remotingMetaRegistryConfigurator(configurator:String){
        folderData.remotingMetaRegistryConfigurator = configurator
    }

    fun remotingTypesConfigurator(configurator:String){
        folderData.remotingTypesConfigurator = configurator
    }

    fun serializationTypes(vararg sources: String) {
        folderData.serializableTypes.add(ElsaTypesRecord().also {
            it.metadataFiles.addAll(sources.map { project.file(it) })
        })
    }
    fun domainTypes(vararg sources: String) {
        folderData.domainTypes.add(ElsaTypesRecord().also {
            it.metadataFiles.addAll(sources.map { project.file(it) })
        })
    }
    fun l10nTypes(vararg sources: String) {
        folderData.l10nTypes.add(ElsaTypesRecord().also {
            it.metadataFiles.addAll(sources.map { project.file(it) })
        })
    }
    fun customTypes(vararg sources: String) {
        folderData.customTypes.add(ElsaTypesRecord().also {
            it.metadataFiles.addAll(sources.map { project.file(it) })
        })
    }

    fun remotingTypes(vararg sources: String) {
        folderData.remotingTypes.add(ElsaTypesRecord().also {
            it.metadataFiles.addAll(sources.map { project.file(it) })
        })
    }

    fun l10nMeta(factory:String, vararg sources: String) {
        val record = ElsaJavaL10nCodeGenRecord();
        record.factory = factory
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.l10nCodeGenRecords.add(record)
    }

    fun domainMeta(vararg sources: String) {
        val record = ElsaJavaDomainCodeGenRecord();
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.domainCodeGenRecords.add(record)
    }
    fun customMeta(vararg sources: String) {
        val record = ElsaJavaCustomCodeGenRecord();
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.customCodeGenRecords.add(record)
    }

    fun remotingMeta(vararg sources: String, configure: ElsaJavaRemotingCodeGenRecord.() -> Unit = {}  ) {
        val record = ElsaJavaRemotingCodeGenRecord()
        sources.forEach {
            record.sources.add(project.file(it))
        }
        record.configure()
        folderData.remotingCodeGenRecords.add(record)
    }

}
