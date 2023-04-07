package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.codegen.ElsaCodeGenTask
import com.gridnine.elsa.gradle.config.ElsaCodeGenExtensionData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension

open class ElsaJavaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val epe = target.rootProject.extensions.findByType(
            ExtraPropertiesExtension::class.java
        )!!
        val codeGenKey = "elsa-codegen-extension-data"
        if (!epe.has(codeGenKey)) {
            epe[codeGenKey] = ElsaCodeGenExtensionData()
        }
        val codeGenData = epe[codeGenKey] as ElsaCodeGenExtensionData
        target.extensions.create(
            "elsa-java-extension",
            ElsaJavaExtension::class.java, target, codeGenData
        )
    }

}
fun Project.elsa(configure: ElsaJavaExtension.() -> Unit): Unit {
    (this as ExtensionAware).extensions.configure("elsa-java-extension", configure)
    this.tasks.create("eCodeGen", ElsaCodeGenTask::class.java)
}
