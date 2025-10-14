plugins {
    id("java-test-fixtures")
    id("elsa-java")
    id("java-library")
    java
}

tasks.compileJava.get().dependsOn(tasks.getByName("eCodeGen"))

elsa {
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
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.springframework:spring-context:6.2.7")
    implementation(project(":platform:common-meta"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation(project(":platform:common-meta"))
    testFixturesImplementation("org.springframework:spring-test:6.2.7")
    testFixturesImplementation("ch.qos.logback:logback-classic:1.5.13")
    testFixturesImplementation("org.junit.platform:junit-platform-suite:1+")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation("org.mockito:mockito-core:5+")
    testFixturesImplementation("org.mockito:mockito-junit-jupiter:5+")
    testFixturesImplementation("org.springframework:spring-context:6.2.7")

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

group ="com.gridnine.elsa"
version ="1.0"