import com.gridnine.elsa.gradle.internal.elsaInternal

buildscript {
    repositories{
        mavenLocal()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle-internal:0+")
    }
}

plugins {
    `java-gradle-plugin`
    kotlin("jvm")  version "1.8.10"
}


gradlePlugin {
    plugins {
        create("elsa-java") {
            id = "elsa-java-configuration"
            implementationClass = "com.gridnine.elsa.gradle.plugin.ElsaJavaConfigurationPlugin"
        }
        create("elsa-java-decoration") {
            id = "elsa-java-decoration"
            implementationClass = "com.gridnine.elsa.gradle.plugin.ElsaJavaDecorationPlugin"
        }
    }
}
repositories{
    mavenLocal()
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin>()

elsaInternal {
    artefactId = "elsa-gradle"
}

dependencies{
    implementation("com.gridnine:elsa-meta:${project.property("version")}")
}

java{
    withSourcesJar()
}
val jarArchiveName = "elsa-gradle"

tasks.withType<Jar>{
    archiveBaseName.set(jarArchiveName)
}

task("publishGradlePluginToLocalMavenRepository"){
    dependsOn("publishToMavenLocal")
    group = "elsa"
}

