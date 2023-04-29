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
    artefactId = "elsa-server-tomcat"
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

dependencies {
    implementation("javax.servlet:javax.servlet-api:4+")
    implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.7")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:10.1.7")
    implementation("org.slf4j:slf4j-api:2+")
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
}
