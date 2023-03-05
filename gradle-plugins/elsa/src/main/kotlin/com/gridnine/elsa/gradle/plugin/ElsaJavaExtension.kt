package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.config.ElsaTypesExtensionData
import org.gradle.api.Project

annotation class ElsaJavaConfigMarker

@ElsaJavaConfigMarker
open class ElsaJavaExtension(private var project: Project, private var data: ElsaTypesExtensionData) {

    val typesExtension = ElsaTypesExtension(this.project, data)

    fun types(configure: ElsaTypesExtension.() -> Unit) {
        typesExtension.configure()
    }
}