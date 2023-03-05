import com.gridnine.elsa.gradle.internal.elsaInternal
import com.gridnine.elsa.gradle.plugin.elsa

plugins {
    java
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
    types{
        destDir("src/main/java-gen")
        serialization("types/core-serialization.xml", "com.gridnine.elsa.core.CoreSerializableTypesConfigurator")
        domain("types/core-domain.xml", "com.gridnine.elsa.core.CoreDomainTypesConfigurator")
        custom("types/core-custom.xml", "com.gridnine.elsa.core.CoreCustomTypesConfigurator")
        l10n("types/core-l10n.xml", "com.gridnine.elsa.core.CoreL10nTypesConfigurator")
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

dependencies {
    implementation("ch.qos.logback:logback-core:1+")
    implementation("org.slf4j:slf4j-api:2+")
    implementation(project(":platform:java-meta"))
}
