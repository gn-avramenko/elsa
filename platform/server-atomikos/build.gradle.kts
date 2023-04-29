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
     artefactId = "elsa-server-atomikos"
}

repositories {
    mavenCentral()
}

dependencies{
    implementation("ch.qos.logback:logback-core:1+")
    implementation("ch.qos.logback:logback-classic:1+")
    implementation("javax.transaction:jta:1.1")
    implementation("com.atomikos:transactions-jdbc:5.0.9")
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testImplementation(testFixtures(project(":platform:common")))
    testImplementation(testFixtures(project(":platform:server")))
    testFixturesImplementation(testFixtures(project(":platform:server")))
    testFixturesImplementation(testFixtures(project(":platform:common")))
}


tasks.test {
    useJUnitPlatform()
}
