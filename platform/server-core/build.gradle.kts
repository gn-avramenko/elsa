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
    implementation("org.apache.commons:commons-lang3:3+")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.fasterxml.jackson.core:jackson-core:2+")
    implementation("com.nothome:javaxdelta:2.0.1")
    implementation("org.ehcache:ehcache:3+")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation("com.mchange:c3p0:0.9.5.5")
    testFixturesImplementation("org.hsqldb:hsqldb:2+")
    testFixturesImplementation("com.mchange:c3p0:0.9.5.5")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-jdbc")
}


sourceSets.main{
    java.srcDirs("src/main/java","src/main/java-gen")
}

//sourceSets.testFixtures{
//    java.srcDirs("src/testFixtures/java-gen")
//}

tasks.test {
    useJUnitPlatform()
}
