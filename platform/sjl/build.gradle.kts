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
    }
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaPlugin>()

elsaInternal {
    artefactId = "elsa-sjl"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()


dependencies {
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(files("lib/sjl.jar"))
}