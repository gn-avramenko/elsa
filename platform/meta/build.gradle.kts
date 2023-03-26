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
    }
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin>()

elsaInternal {
    artefactId = "elsa-meta"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.slf4j:slf4j-api:2+")
}

task("publishJavaMetaToLocalMavenRepository"){
    group="elsa"
    dependsOn("publishToMavenLocal")
}

