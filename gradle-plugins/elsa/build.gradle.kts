import com.gridnine.elsa.gradle.internal.elsaInternal

buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
    }
}

plugins {
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

task("publishLocalArtifacts"){
    dependsOn("jar")
    group = "elsa"
    doLast {
        project.file("build/libs/elsa-gradle-${project.property("version")}.jar").copyTo(project.file("../../gradle/elsa-gradle.jar"), overwrite = true)
    }
}


