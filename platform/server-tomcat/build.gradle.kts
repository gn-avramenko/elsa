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
