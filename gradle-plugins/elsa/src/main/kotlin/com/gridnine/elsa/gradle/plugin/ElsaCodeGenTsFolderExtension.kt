package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsFolderData
import com.gridnine.elsa.gradle.config.ElsaTsCustomCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTsDomainCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTsL10nCodeGenRecord
import com.gridnine.elsa.gradle.config.ElsaTsRemotingCodeGenRecord
import org.gradle.api.Project
import java.io.File

@ElsaTsConfigMarker
open class ElsaCodeGenTsFolderExtension(private val project:Project, private val folderData:ElsaCodeGenTsFolderData) {

    fun remoting(module: String, vararg sources: String) {
        val record = ElsaTsRemotingCodeGenRecord()
        record.module = File(folderData.folder, "$module.ts")
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.remotingCodeGenRecords.add(record)
    }

    fun remoting(configure:ElsaCodeGenTsRemotingExtension.() -> Unit) {
        val record = ElsaTsRemotingCodeGenRecord()
        ElsaCodeGenTsRemotingExtension(project, folderData, record).configure()
        folderData.remotingCodeGenRecords.add(record)
    }

    fun l10n(module: String, vararg sources: String) {
        val record = ElsaTsL10nCodeGenRecord()
        record.module = File(folderData.folder, "$module.ts")
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.l10nCodeGenRecords.add(record)
    }

    fun domain(module: String, vararg sources: String) {
        val record = ElsaTsDomainCodeGenRecord()
        record.module = File(folderData.folder, "$module.ts")
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.domainCodeGenRecords.add(record)
    }

    fun custom(module: String, vararg sources: String) {
        val record = ElsaTsCustomCodeGenRecord()
        record.module = File(folderData.folder, "$module.ts")
        sources.forEach {
            record.sources.add(project.file(it))
        }
        folderData.customCodeGenRecords.add(record)
    }

}
