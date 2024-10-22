plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.5"
}
buildscript {
    dependencies {
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/gradle-plugin.jar")))
    }
}
repositories {
    mavenCentral()
}

apply<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaPlugin>()

configure<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaExtension> {
    codegen {
        remoting(
            "src/main/java-gen",
            "com.gridnine.platform.elsa.config.ElsaDemoRemotingMetaRegistryConfigurator",
            "com.gridnine.platform.elsa.config.ElsaDemoRemotingConstants",
            false,
            arrayListOf("src/main/codegen/demo-remoting.xml")
        )

    }
}
sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

dependencies {
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-postgres"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

}
