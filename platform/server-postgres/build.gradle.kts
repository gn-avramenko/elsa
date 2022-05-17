plugins{
    id("org.springframework.boot") version "2.6.3"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
    java
}
buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle-internal.jar"),
            File(projectDir.parentFile.parentFile, "gradle/elsa-gradle.jar")))
    }
}
apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.internal.ElsaInternalJavaExtension>{
    artefactId = "elsa-server-postgres"
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()


apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

dependencies{
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("com.mchange:c3p0:0.9.5.5")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation(project(":platform:server-core"))
    implementation(project(":platform:common-core"))
}
