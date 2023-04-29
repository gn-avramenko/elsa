import com.gridnine.elsa.gradle.internal.elsaInternal

plugins {
    java
    id("java-test-fixtures")
}
buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
        classpath(files(project.file("../../gradle/elsa-gradle.jar")))
    }
}


apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin>()

elsaInternal {
    artefactId = "elsa-server-jms"
}

dependencies{
    implementation("ch.qos.logback:logback-core:1+")
    implementation("ch.qos.logback:logback-classic:1+")
    implementation("com.atomikos:transactions-jms:5.0.9")
    implementation("org.apache.activemq:artemis-server:2.28.0")
    implementation("org.apache.activemq:artemis-jms-server:2.28.0")
    implementation("org.apache.activemq:artemis-jms-client:2.28.0")
    implementation("javax.transaction:jta:1.1")
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
    implementation(project(":platform:server-atomikos"))
    implementation("javax.resource:connector-api:1.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testImplementation(testFixtures(project(":platform:common")))
    testImplementation(testFixtures(project(":platform:server")))
    testImplementation(testFixtures(project(":platform:server-atomikos")))
    testFixturesImplementation(testFixtures(project(":platform:server")))
    testFixturesImplementation(testFixtures(project(":platform:common")))
    testFixturesImplementation(testFixtures(project(":platform:server")))
    testFixturesImplementation(testFixtures(project(":platform:server-atomikos")))
}


tasks.test {
    useJUnitPlatform()
}
