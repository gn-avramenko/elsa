import com.gridnine.elsa.gradle.plugin.elsa
import com.gridnine.elsa.gradle.plugin.elsaTS

//System.setProperty("maven.repo.local", project.file("maven-local").absolutePath)

plugins {
    java
}

buildscript {
    dependencies{
        classpath(files(project.file("gradle/elsa-meta.jar")))
        classpath(files(project.file("gradle/elsa-gradle.jar")))
    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()
apply<com.gridnine.elsa.gradle.plugin.ElsaTsPlugin>()
elsa{

}
elsaTS {

}

task("publishLocalArtifacts"){
    group = "elsa"
}