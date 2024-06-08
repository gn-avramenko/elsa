plugins {
    id("java-test-fixtures")
    java
}
buildscript {
    dependencies {
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/gradle-plugin.jar")))
    }
}
apply<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaPlugin>()
tasks.compileJava.get().dependsOn(tasks.getByName("eCodeGen"))

configure<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaExtension> {
    codegen {
        domain(
            "src/testFixtures/java-gen",
            "com.gridnine.platform.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator",
            arrayListOf("src/testFixtures/codegen/elsa-core-test-domain.xml")
        )
        custom(
            "src/main/java-gen",
            "com.gridnine.platform.elsa.config.ElsaCommonCoreCustomMetaRegistryConfigurator",
            arrayListOf("src/main/codegen/elsa-core-custom.xml")
        )
        remoting(
            "src/main/java-gen",
            "com.gridnine.platform.elsa.config.ElsaCommonCoreRemotingMetaRegistryConfigurator",
            arrayListOf("src/main/codegen/elsa-core-remoting.xml")
        )
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.springframework:spring-context:6.0.11")
    implementation(project(":platform:common-meta"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation(project(":platform:common-meta"))
    testFixturesImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation("ch.qos.logback:logback-classic:1.4.12")
    testFixturesImplementation("org.junit.platform:junit-platform-suite:1+")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation("org.mockito:mockito-core:5+")
    testFixturesImplementation("org.mockito:mockito-junit-jupiter:5+")
    testFixturesImplementation("org.springframework:spring-context:6.0.11")

}


sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

sourceSets.testFixtures {
    java.srcDirs("src/testFixtures/java-gen")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
