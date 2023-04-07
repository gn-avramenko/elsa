package com.gridnine.elsa.gradle.plugin

import com.gridnine.elsa.gradle.codegen.ElsaCodeGenTask
import com.gridnine.elsa.gradle.config.ElsaCodeGenTsExtensionData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension

open class ElsaTsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val epe = target.rootProject.extensions.findByType(
            ExtraPropertiesExtension::class.java
        )!!
        val tsCodeGenKey = "elsa-codegen-ts-extension-data"
        if (!epe.has(tsCodeGenKey)) {
            epe[tsCodeGenKey] = ElsaCodeGenTsExtensionData()
        }
        val tsCodeGenData = epe[tsCodeGenKey] as ElsaCodeGenTsExtensionData
        target.extensions.create(
            "elsa-ts-extension",
            ElsaTsExtension::class.java, target, tsCodeGenData
        )
    }

}
fun Project.elsaTS(configure: ElsaTsExtension.() -> Unit): Unit {
    (this as ExtensionAware).extensions.configure("elsa-ts-extension", configure)
    this.tasks.create("eCodeGen", ElsaCodeGenTask::class.java)
}