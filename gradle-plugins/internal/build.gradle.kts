plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("elsa-internal-configuration") {
            id = "elsa-internal-java-configuration"
            implementationClass = "com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin"
        }
        create("elsa-internal-java-decoration") {
            id = "elsa-internal-java"
            implementationClass = "com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin"
        }
    }
}


java{
    sourceCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

val jarArchiveName = "elsa-gradle-internal"

tasks.withType<Jar>{
    archiveBaseName.set(jarArchiveName)
}

task("updateLocalGradlePlugins"){
    dependsOn("build")
    group = "other"
    doLast{
        val gradleDir = File(projectDir.parentFile.parentFile, "gradle")
        project.file("build/libs/${jarArchiveName}.jar").copyTo(File(gradleDir, "${jarArchiveName}.jar"), true)
    }
}

