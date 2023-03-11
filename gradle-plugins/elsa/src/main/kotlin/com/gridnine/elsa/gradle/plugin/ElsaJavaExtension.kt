package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenExtensionData
import org.gradle.api.Project

annotation class ElsaJavaConfigMarker

@ElsaJavaConfigMarker
open class ElsaJavaExtension(project: Project, codeGenData: ElsaCodeGenExtensionData) {

    val codeGenExtension = ElsaCodeGenExtension(project, codeGenData)

    fun codegen(configure: ElsaCodeGenExtension.() -> Unit) {
        codeGenExtension.configure()
    }
}