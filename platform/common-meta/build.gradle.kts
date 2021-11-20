buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle-platform.jar")))
    }
}

apply<com.gridnine.elsa.gradle.platform.ElsaPlatformExtensionPlugin>()

configure<com.gridnine.elsa.gradle.platform.ElsaPlatformExtension> {
    artefactId = "elsa-common-meta"
}

apply<com.gridnine.elsa.gradle.platform.ElsaPlatformConfigurationPlugin>()

//plugins {
//    java
//    `java-library`
//    `maven-publish`
//}
//
//group = "com.gridnine"
//version = "0.0.1"
//
//
//java{
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_1_8
//    withSourcesJar()
//}
//tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
//    options.compilerArgs.addAll(arrayListOf("--release", "8"))
//}

//tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
//    this.configure<org.gradle.api.tasks.compile.CompileOptions>{
//        compilerArgs.addAll(arrayListOf("--release", "1.8"))
//    }
//
//}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            artifactId = "elsa-common-meta"
//            from(components["java"])
//        }
//    }
//    repositories {
//
//        maven {
//            credentials {
//                username = "admin"
//                password = "admin"
//            }
//            url = uri("http://localhost:18087/repository/maven-releases/")
//            isAllowInsecureProtocol = true
//        }
//    }
//}
//
