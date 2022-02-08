plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("elsa-java-configuration") {
            id = "elsa-java-configuration"
            implementationClass = "com.gridnine.elsa.gradle.plugin.ElsaJavaConfigurationPlugin"
        }
        create("elsa-java-decoration") {
            id = "elsa-java"
            implementationClass = "com.gridnine.elsa.gradle.plugin.ElsaJavaDecorationPlugin"
        }
    }
}

dependencies{
    implementation(project(":platform:common-meta"))
}
java{
    sourceCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

val jarArchiveName = "elsa-gradle"

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

