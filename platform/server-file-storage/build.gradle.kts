plugins {
    java
    id("java-test-fixtures")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("net.java.xadisk:xadisk:1.2.2@jar")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.springframework:spring-context:6.2.7")
    implementation("com.atomikos:transactions-jta:6.0.0:jakarta")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("javax.resource:connector-api:1.5")
    implementation("org.springframework:spring-tx:6.2.7")
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
    testFixturesImplementation("org.springframework:spring-test:6.2.7")
    testFixturesImplementation("org.springframework:spring-context:6.2.7")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5+")
}


group ="com.gridnine.elsa"
version ="1.0"

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
