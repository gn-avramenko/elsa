buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("elsa-internal-configuration") {
            id = "elsa-internal-configuration"
            implementationClass = "com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin"
        }
        create("elsa-internal-java-decoration") {
            id = "elsa-internal-java-decoration"
            implementationClass = "com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.gridnine"
            artifactId = "elsa-gradle-internal"
            version = "0.0.1"

            from(components["java"])
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

task("publishInternalGradlePluginToLocalMavenRepository"){
    group = "elsa"
    dependsOn("publishToMavenLocal")
}

