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
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("javax.transaction:jta:1.1")
    implementation("org.springframework:spring-context:6.0.11")
    implementation("com.atomikos:atomikos-util:6.0.0")
    implementation("com.atomikos:transactions-jdbc:6.0.0:jakarta")
    implementation("com.atomikos:transactions-jta:6.0.0:jakarta")
    implementation("org.springframework:spring-tx:6.0.11")
    implementation("org.springframework:spring-jdbc:6.0.11")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation(testFixtures(project(":platform:common-core")))
    testImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation("org.springframework:spring-context:6.0.11")
    testFixturesImplementation("org.springframework:spring-test:6.0.11")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")

}


tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
