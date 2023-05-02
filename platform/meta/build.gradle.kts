import com.gridnine.elsa.gradle.internal.elsaInternal

plugins {
    java
}
buildscript {
    dependencies{
            classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
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

task("publishLocalArtifacts"){
    dependsOn("jar")
    group = "elsa"
    doLast {
        project.file("build/libs/meta-${project.property("version")}.jar").copyTo(project.file("../../gradle/elsa-meta.jar"), overwrite = true)
    }
}



