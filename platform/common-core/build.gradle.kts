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
    artefactId = "elsa-common-core"
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()


apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaJavaExtension>{
    codegen {
        domain("src/testFixtures/codegen/elsa-core-test-domain.xml",
            "src/testFixtures/java-gen",
            "com.gridnine.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator")
        rest("src/testFixtures/codegen/elsa-core-test-rest.xml",
            "src/testFixtures/java-gen",
            "com.gridnine.elsa.common.core.test.ElsaCommonCoreTestRestMetaRegistryConfigurator")
        custom("src/main/codegen/elsa-core-custom.xml",
            "src/main/java-gen",
            "com.gridnine.elsa.common.core.ElsaCommonCoreCustomMetaRegistryConfigurator")
    }
}

dependencies{
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.core:jackson-core:2.10.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(project(":platform:common-meta"))
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation(project(":platform:common-meta"))
}
//val jar: Jar by tasks
//val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
//
//bootJar.enabled = false
//jar.enabled = true
//tasks.named<Jar>("sourcesJar") {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

sourceSets.main{
    java.srcDirs("src/main/java","src/main/java-gen")
}

sourceSets.testFixtures{
    java.srcDirs("src/testFixtures/java-gen")
}

tasks.test {
    useJUnitPlatform()
}
