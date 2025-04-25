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
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.springframework:spring-context:6.0.11")
    implementation("org.springframework.data:spring-data-mongodb:4.1.2")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation(testFixtures(project(":platform:common-core")))
    testImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation(project(":platform:common-meta"))
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation("org.springframework.data:spring-data-mongodb:4.1.2")
    testFixturesImplementation("org.springframework:spring-context:6.0.11")
    testFixturesImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation("de.bwaldvogel:mongo-java-server:1.44.0")
}

elsa {
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
