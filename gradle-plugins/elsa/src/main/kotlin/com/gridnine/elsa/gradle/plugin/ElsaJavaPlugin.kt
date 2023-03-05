package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.codegen.ElsaCodeGenTask
import com.gridnine.elsa.gradle.config.ElsaTypesExtensionData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension

open class ElsaJavaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val epe = target.rootProject.extensions.findByType(
            ExtraPropertiesExtension::class.java
        )!!
        val key = "elsa-types-extension-data"
        if (!epe.has(key)) {
            epe[key] = ElsaTypesExtensionData()
        }
        val data = epe[key] as ElsaTypesExtensionData?
        target.extensions.create(
            "elsa-java-extension",
            ElsaJavaExtension::class.java, target, data
        )
    }

    companion object {
        fun decorate(target: Project) {
            target.tasks.create("eCodeGen", ElsaCodeGenTask::class.java)
        }
    }
}
fun Project.elsa(configure: ElsaJavaExtension.() -> Unit): Unit {
    (this as ExtensionAware).extensions.configure("elsa-java-extension", configure)
    ElsaJavaPlugin.decorate(this);
}