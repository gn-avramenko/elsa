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

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.internal.ElsaInternalJavaExtension>{
    artefactId = "elsa-core"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaTypesExtension> {
    destDir("src/main/java-gen")
    serialization("types/core-serialization.xml", "com.gridnine.elsa.core.SerializableTypeConfigurator")
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()




