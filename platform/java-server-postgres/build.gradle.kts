import com.gridnine.elsa.gradle.internal.elsaInternal

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
    artefactId = "elsa-server-postgres"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

dependencies {
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("com.mchange:c3p0:0.9.5.5")
    implementation(project(":platform:java-meta"))
    implementation(project(":platform:java-core"))
    implementation(project(":platform:java-server"))
}
