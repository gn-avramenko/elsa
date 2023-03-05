package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaTypesExtensionData
import com.gridnine.elsa.gradle.config.ElsaTypesFileData
import com.gridnine.elsa.gradle.config.ElsaTypesProjectData
import org.gradle.api.Project

@ElsaJavaConfigMarker
open class ElsaTypesExtension(private val project: Project, val data: ElsaTypesExtensionData) {

    private fun findItem(): ElsaTypesProjectData {
        var item = data.items.stream().filter { it: ElsaTypesProjectData -> it.project == project }
            .findFirst().orElse(null)
        if (item == null) {
            item = ElsaTypesProjectData()
            item.project = project
            data.items.add(item)
        }
        return item
    }

    fun destDir(dir: String?) {
        findItem().destDir = project.file(dir)
    }

    fun serialization(path: String?, configuratorClassName: String?) {
        findItem().serializableTypes.add(ElsaTypesFileData(project.file(path), configuratorClassName))
    }

    fun domain(path: String?, configuratorClassName: String?) {
        findItem().domainTypes.add(ElsaTypesFileData(project.file(path), configuratorClassName))
    }

    fun custom(path: String?, configuratorClassName: String?) {
        findItem().customTypes.add(ElsaTypesFileData(project.file(path), configuratorClassName))
    }

    fun l10n(path: String?, configuratorClassName: String?) {
        findItem().l10nTypes.add(ElsaTypesFileData(project.file(path), configuratorClassName))
    }

    fun xsdsLocation(path: String?) {
        data.xsdsLocation = project.file(path)
    }

    fun xsdsCustomizationSuffix(suffix: String?) {
        data.xsdsCustomizationSuffix = suffix
    }
}
