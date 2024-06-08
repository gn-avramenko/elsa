plugins {
    java
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":platform:common-meta"))
    implementation(gradleApi())
}

java {
    withSourcesJar()
}

val jarArchiveName = "gradle-plugin"

tasks.withType<Jar> {
    dependsOn(":platform:common-meta:compileJava")
    archiveBaseName.set(jarArchiveName)
    from(files(project.file("../common-meta/build/classes/java/main")))
}

task("updateLocalGradlePlugins") {
    dependsOn("build")
    group = "other"
    doLast {
        val gradleDir = File(projectDir.parentFile.parentFile, "gradle")
        project.file("build/libs/${jarArchiveName}.jar").copyTo(File(gradleDir, "${jarArchiveName}.jar"), true)
    }
}

