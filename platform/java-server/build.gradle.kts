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
    artefactId = "elsa-java-server"
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
    implementation(project(":platform:java-meta"))
    implementation(project(":platform:java-core"))
}
