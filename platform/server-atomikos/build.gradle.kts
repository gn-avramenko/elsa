plugins {
    java
    id("java-test-fixtures")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("javax.transaction:jta:1.1")
    implementation("org.springframework:spring-context:6.2.7")
    implementation("com.atomikos:atomikos-util:6.0.0")
    implementation("com.atomikos:transactions-jdbc:6.0.0:jakarta")
    implementation("com.atomikos:transactions-jta:6.0.0:jakarta")
    implementation("org.springframework:spring-tx:6.2.7")
    implementation("org.springframework:spring-jdbc:6.2.7")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    testImplementation("org.junit.platform:junit-platform-suite:1+")
    testImplementation("org.junit.jupiter:junit-jupiter:5+")
    testImplementation(testFixtures(project(":platform:common-core")))
    testImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation(testFixtures(project(":platform:common-core")))
    testFixturesImplementation(testFixtures(project(":platform:server-core")))
    testFixturesImplementation("org.springframework:spring-context:6.2.7")
    testFixturesImplementation("org.springframework:spring-test:6.2.7")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")

}

group ="com.gridnine.elsa"
version ="1.0"

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
