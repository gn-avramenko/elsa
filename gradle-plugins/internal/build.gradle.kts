
buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm")  version "1.8.10"
//    `java-gradle-plugin`
    `maven-publish`
}

repositories{
    mavenCentral()
}

//gradlePlugin {
//    plugins {
//        create("elsa-internal") {
//            id = "elsa-internal"
//            version = "0.0.1"
//            implementationClass = "com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin"
//        }
//    }
//}

dependencies{
    implementation(gradleApi())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.gridnine"
            artifactId = "elsa-gradle-internal"
            version = "0.0.1"

            from(components["kotlin"])
        }
    }
}

java{
    sourceCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

val jarArchiveName = "elsa-gradle-internal"
group = "com.gridnine"

tasks.withType<Jar>{
    archiveBaseName.set(jarArchiveName)
}

task("publishLocalArtifacts"){
    dependsOn("jar")
    group = "elsa"
    doLast {
        project.file("build/libs/elsa-gradle-internal.jar").copyTo(project.file("../../gradle/elsa-gradle-internal.jar"), overwrite = true)
    }
}
