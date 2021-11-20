plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("elsa-platform-configuration") {
            id = "elsa-platform-configuration"
            implementationClass = "com.gridnine.elsa.gradle.platform.ElsaPlatformConfigurationPlugin"
        }
        create("elsa-platform-extension") {
            id = "elsa-platform-extension"
            implementationClass = "com.gridnine.elsa.gradle.platform.ElsaPlatformExtensionPlugin"
        }
    }
}


java{
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}
tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
    options.compilerArgs.addAll(arrayListOf("--release", "8"))
}

val jarArchiveName = "elsa-gradle-platform"

tasks.withType<Jar>{
    archiveBaseName.set(jarArchiveName)
}

task("publish"){
    dependsOn("build")
    group = "publishing"
    doLast{
        val env = findProperty("env")?:"dev"
        if(env == "dev"){
           val gradleDir = File(projectDir.parentFile.parentFile, "gradle")
           project.file("build/libs/${jarArchiveName}.jar").copyTo(File(gradleDir, "${jarArchiveName}.jar"), true)
        }
    }
}