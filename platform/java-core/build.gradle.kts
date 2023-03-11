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
    artefactId = "elsa-java-core"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

elsa{
    codegen {
        xsdLocation("src/main/resources/xsds")
        folder("src/main/java-gen"){
            serializableTypesConfigurator("com.gridnine.elsa.core.CoreSerializableTypesConfigurator")
            domainTypesConfigurator("com.gridnine.elsa.core.CoreDomainTypesConfigurator")
            customTypesConfigurator("com.gridnine.elsa.core.CoreCustomTypesConfigurator")
            l10nTypesConfigurator("com.gridnine.elsa.core.CoreL10nTypesConfigurator")
            customMetaRegistryConfigurator("com.gridnine.elsa.config.ElsaCommonCoreCustomMetaRegistryConfigurator")
            serializationTypes("src/main/codegen/types-core-serialization.xml")
            domainTypes("src/main/codegen/types-core-domain.xml")
            customTypes("src/main/codegen/types-core-custom.xml")
            l10nTypes("src/main/codegen/types-core-l10n.xml")
            customMeta("src/main/codegen/elsa-core-custom.xml")
        }
        folder("src/testFixtures/java-gen"){
            domainMetaRegistryConfigurator("com.gridnine.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator")
            domainMeta("src/testFixtures/codegen/elsa-core-test-domain.xml")
        }
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

sourceSets.testFixtures{
    java.srcDirs("src/testFixtures/java-gen")
}

dependencies {
    implementation("ch.qos.logback:logback-core:1+")
    implementation("ch.qos.logback:logback-classic:1+")
    implementation("com.fasterxml.jackson.core:jackson-core:2+")
    implementation("org.slf4j:slf4j-api:2+")
    implementation(project(":platform:java-meta"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testImplementation("ch.qos.logback:logback-core:1+")
    testFixturesImplementation(project(":platform:java-meta"))
    testFixturesImplementation("org.slf4j:slf4j-api:2+")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-engine:5+")
}
