package com.gridnine.elsa.gradle.internal

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

@DslMarker
annotation class LundaRuConfigMarker

open class ElsaInternalJavaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            "elsa-internal-java-configuration",
            ElsaInternalJavaExtension::class.java
        )
    }

}

private fun decorate(target: Project) {
    val configDir: File = findConfigDir(target.projectDir)!!
    val file = File(configDir, "config.properties")
    if (!file.exists()) {
        val file2 = File(configDir, "config.properties.template")
        check(file2.exists()) { "file config/config.properties or config/config.properties.template must exist" }
        try {
            Files.copy(file2.toPath(), file.toPath())
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }
    }
    val props = Properties()
    try {
        props.load(StringReader(Files.readString(file.toPath(), StandardCharsets.UTF_8)))
    } catch (e: IOException) {
        throw IllegalStateException(e)
    }
    target.repositories.add(target.repositories.mavenCentral())
    target.plugins.apply("java-library")
    target.plugins.apply("maven-publish")
    target.setProperty("group", "com.gridnine")
    target.setProperty("version", if (props.getProperty("version") == null) "0.0.1" else props.getProperty("version"))
    target.extensions.configure("java") { ext: JavaPluginExtension ->
        ext.sourceCompatibility = JavaVersion.VERSION_17
        ext.targetCompatibility = JavaVersion.VERSION_17
        ext.withSourcesJar()
    }
    val ext = target.extensions.findByName("elsa-internal-java-configuration") as ElsaInternalJavaExtension?
    if (ext != null) {
        val publishing = (target.extensions.findByName("publishing") as PublishingExtension?)!!
        val mp = publishing.publications.create(
            "mavenJava",
            MavenPublication::class.java
        )
        mp.from(target.components.findByName("java"))
        mp.artifactId = ext.artefactId
        publishing.repositories.maven { mavenArtifactRepository: MavenArtifactRepository ->
            mavenArtifactRepository.credentials.username = props.getProperty("maven.username")
            mavenArtifactRepository.credentials.password = props.getProperty("maven.password")
            mavenArtifactRepository.isAllowInsecureProtocol = true
            try {
                mavenArtifactRepository.url = URI(props.getProperty("maven.url"))
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException("bad parameter maven.url", e)
            }
        }
    }
    try {
        target.tasks.getByName("jar").enabled = true
    } catch (e: Exception) {
        //noops
    }
    try {
        target.tasks.getByName("bootJar").enabled = false
    } catch (e: Exception) {
        //noops
    }
    try {
        target.tasks.getByName(
            "sourcesJar"
        ) { task: Task ->
            (task as Jar).duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    } catch (e: Exception) {
        //noops
    }
}
private fun findConfigDir(dir: File?): File? {
    if (dir == null) {
        return null
    }
    val file = File(dir, "config")
    return if (file.exists()) {
        file
    } else findConfigDir(dir.parentFile)
}

fun Project.elsaInternal(configure: ElsaInternalJavaExtension.() -> Unit): Unit {
    (this as ExtensionAware).extensions.configure("elsa-internal-java-configuration", configure)
    decorate(this);
}