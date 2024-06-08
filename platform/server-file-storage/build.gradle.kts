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


dependencies {
    implementation(group = "net.java.xadisk", name = "xadisk", version = "1.2.2", ext = "jar")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.springframework:spring-context:6.0.11")
    implementation("com.atomikos:transactions-jta:6.0.0:jakarta")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("javax.resource:connector-api:1.5")
    implementation("org.springframework:spring-tx:6.0.11")
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation(testFixtures(project(":platform:common-core")))
    testImplementation(testFixtures(project(":platform:server-core")))
    testImplementation(testFixtures(project(":platform:server-atomikos")))
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation(testFixtures(project(":platform:server-atomikos")))
    testFixturesImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation("org.springframework:spring-context:6.0.11")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
}


tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
