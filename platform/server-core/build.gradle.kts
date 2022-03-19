plugins{
    id("org.springframework.boot") version "2.6.3"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("java-test-fixtures")
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
    artefactId = "elsa-server-core"
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()


apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaJavaExtension>{
  codegen {
    l10n("src/main/codegen/core-server-l10n-messages.xml","src/main/java-gen",
    "com.gridnine.elsa.server.core.CoreL10nMessagesRegistryConfigurator",
        "com.gridnine.elsa.server.core.CoreL10nMessagesRegistryFactory"
    )
  }
}

dependencies{
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
}


sourceSets.main{
    java.srcDirs("src/main/java","src/main/java-gen")
}

tasks.test {
    useJUnitPlatform()
}
