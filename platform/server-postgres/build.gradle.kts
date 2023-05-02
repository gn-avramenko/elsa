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
    artefactId = "elsa-server-postgres"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

dependencies {
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("javax.servlet:javax.servlet-api:4+")
    implementation("com.mchange:c3p0:0.9.5.5")
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
}
