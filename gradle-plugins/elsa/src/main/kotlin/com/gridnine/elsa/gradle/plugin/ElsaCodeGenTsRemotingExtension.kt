package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsFolderData
import com.gridnine.elsa.gradle.config.ElsaTsCustomCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTsDomainCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTsRemotingCodeGenRecord
import org.gradle.api.Project
import java.io.File

@ElsaTsConfigMarker
open class ElsaCodeGenTsRemotingExtension(private val project:Project, private val folderData:ElsaCodeGenTsFolderData, private val record:ElsaTsRemotingCodeGenRecord) {

    fun module(module:String) {
        record.module =  File(folderData.folder, "$module.ts")
    }

    fun skipClientGeneration(value: Boolean){
        record.isSkipClientGeneration = value
    }

    fun sources(vararg sources: String){
        sources.forEach {
            record.sources.add(project.file(it))
        }
    }

}
