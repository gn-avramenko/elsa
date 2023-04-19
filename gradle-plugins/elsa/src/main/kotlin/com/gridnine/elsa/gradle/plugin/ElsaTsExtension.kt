package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaCodeGenTsExtensionData
import org.gradle.api.Project

annotation class ElsaTsConfigMarker

@ElsaTsConfigMarker
open class ElsaTsExtension(project: Project, codeGenData: ElsaCodeGenTsExtensionData) {

    lateinit var packageName:String;

    val codeGenExtension = ElsaCodeGenTsExtension(project, codeGenData)

    fun codegen(configure: ElsaCodeGenTsExtension.() -> Unit) {
        codeGenExtension.configure()
    }
}