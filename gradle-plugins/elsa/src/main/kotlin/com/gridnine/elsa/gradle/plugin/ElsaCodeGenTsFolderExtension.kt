package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsFolderData
import com.gridnine.elsa.gradle.config.ElsaTsRemotingCodeGenRecord
import org.gradle.api.Project

@ElsaTsConfigMarker
open class ElsaCodeGenTsFolderExtension(private val project:Project, private val folderData:ElsaCodeGenTsFolderData) {

    fun remoting(module: String, vararg sources: String) {
        val record = ElsaTsRemotingCodeGenRecord()
        record.module = project.file(module)
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.remotingCodeGenRecords.add(record)
    }

}
