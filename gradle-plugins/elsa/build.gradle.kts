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
//    `java-gradle-plugin`
    kotlin("jvm")  version "1.8.10"
}


//gradlePlugin {
//    plugins {
//        create("elsa") {
//            id = "elsa"
//            version = "0.0.1"
//            implementationClass = "com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin"
//        }
//    }
//}

repositories{
    mavenLocal()
    mavenCentral()
}

dependencies{
    implementation(gradleApi())
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

