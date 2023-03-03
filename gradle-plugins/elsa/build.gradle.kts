plugins {
    `java-gradle-plugin`
}

buildscript {
    repositories{
        mavenLocal()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle-internal:0+")
    }
}

gradlePlugin {
    plugins {
        create("elsa-java-configuration") {
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

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.internal.ElsaInternalJavaExtension>{
    artefactId = "elsa-gradle"
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()

dependencies{
    implementation("com.gridnine:elsa-meta:${project.property("version")}")
}

java{
    sourceCompatibility = JavaVersion.VERSION_17
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

