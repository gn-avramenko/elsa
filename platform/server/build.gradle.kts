import com.gridnine.elsa.gradle.internal.elsaInternal
import com.gridnine.elsa.gradle.plugin.elsa

plugins {
    java
    id("java-test-fixtures")
}
buildscript {
    repositories{
        mavenLocal()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle-internal:0+")
        classpath("com.gridnine:elsa-gradle:0+")
    }
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin>()

elsaInternal {
    artefactId = "elsa-server"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

elsa{
    codegen {
        folder("src/main/java-gen"){
            l10nMetaRegistryConfigurator("com.gridnine.elsa.server.ServerL10nMessagesRegistryConfigurator")
            l10nMeta("com.gridnine.elsa.server.ServerL10nMessagesRegistryFactory", "src/main/codegen/core-server-l10n-messages.xml")
        }
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}


dependencies {
    implementation("ch.qos.logback:logback-core:1+")
    implementation("ch.qos.logback:logback-classic:1+")
    implementation("com.fasterxml.jackson.core:jackson-core:2+")
    implementation("com.nothome:javaxdelta:2.0.1")
    implementation("org.ehcache:ehcache:3+")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testImplementation(testFixtures(project(":platform:common")))
    testImplementation("com.mchange:c3p0:0.9.5.5")
    testImplementation("org.hsqldb:hsqldb:2+")
    testFixturesImplementation(project(":platform:meta"))
    testFixturesImplementation(testFixtures(project(":platform:common")))
    testFixturesImplementation("com.mchange:c3p0:0.9.5.5")
    testFixturesImplementation("org.hsqldb:hsqldb:2+")
}
