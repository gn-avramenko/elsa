plugins {
    java
    id("java-test-fixtures")
    id("elsa-java")
}


repositories {
    mavenCentral()
}
tasks.compileJava.get().dependsOn(tasks.getByName("eCodeGen"))

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.springframework:spring-context:6.2.7")
    implementation("org.springframework.data:spring-data-mongodb:4.1.2")
    implementation(project(":common-meta"))
    implementation(project(":common-core"))
    implementation(project(":server-core"))
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation(testFixtures(project(":common-core")))
    testImplementation(testFixtures(project(":server-core")))
    testFixturesImplementation(project(":common-meta"))
    testFixturesImplementation(testFixtures(project(":common-core")))
    testFixturesImplementation(testFixtures(project(":server-core")))
    testFixturesImplementation("org.springframework.data:spring-data-mongodb:4.1.2")
    testFixturesImplementation("org.springframework:spring-context:6.2.7")
    testFixturesImplementation("org.springframework:spring-test:6.2.7")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation("de.bwaldvogel:mongo-java-server:1.47.0")
}

configure<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaExtension> {
    codegen {
        domain(
            "src/testFixtures/java-gen",
            "com.gridnine.platform.elsa.server.mongo.test.ElsaServerMongoTestDomainMetaRegistryConfigurator",
            arrayListOf("src/testFixtures/codegen/elsa-mongo-test-domain.xml")
        )
        custom(
            "src/main/java-gen",
            "com.gridnine.platform.elsa.common.mongo.ElsaServerMongoCustomMetaRegistryConfigurator",
            arrayListOf("src/main/codegen/elsa-mongo-custom.xml")
        )
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

sourceSets.testFixtures {
    java.srcDirs("src/testFixtures/java-gen")
}

group ="com.gridnine.elsa"
version ="1.0"

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
