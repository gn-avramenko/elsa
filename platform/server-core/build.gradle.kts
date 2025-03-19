plugins {
    java
    id("java-test-fixtures")
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
tasks.compileJava.get().dependsOn(tasks.getByName("eCodeGen"))

configure<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaExtension> {
    codegen {
        l10n(
            "src/main/java-gen",
            "com.gridnine.platform.elsa.server.core.CoreL10nMessagesRegistryConfigurator",
            "com.gridnine.platform.elsa.server.core.CoreL10nMessagesRegistryFactory",
            arrayListOf("src/main/codegen/core-server-l10n-messages.xml")
        )
        domain(
                "src/main/java-gen", "com.gridnine.platform.elsa.server.core.CoreDomainConfigurator",
           arrayListOf("src/main/codegen/core-server-domain.xml")
        )
        remoting(
            "src/main/java-gen", null,
            "com.gridnine.platform.elsa.server.core.CoreRemotingConstants", true,
            arrayListOf("../common-core/src/main/codegen/elsa-core-remoting.xml")
        )
    }
}

dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.springframework:spring-webmvc:6.0.11")
    implementation("org.springframework:spring-context:6.0.11")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation("com.nothome:javaxdelta:2.0.1")
    implementation("org.ehcache:ehcache:3.9.9")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springframework:spring-jdbc:6.0.11")
    implementation("org.springframework:spring-tx:6.0.11")
    implementation("io.swagger.core.v3:swagger-models-jakarta:2.2.9")
    implementation("io.swagger.core.v3:swagger-core-jakarta:2.2.9")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation(testFixtures(project(":platform:common-core")))
    testImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation("org.springframework:spring-jdbc:6.0.11")
    testFixturesImplementation("org.springframework:spring-context:6.0.11")
    testFixturesImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation("com.mchange:c3p0:0.9+")
    testFixturesImplementation("org.hsqldb:hsqldb:2+")
}


sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

group ="com.gridnine.elsa"
version ="1.0"
tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
