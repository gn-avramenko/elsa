import com.gridnine.elsa.gradle.internal.elsaInternal

plugins {
    java
}
buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
        classpath(files(project.file("../../gradle/elsa-gradle.jar")))
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
