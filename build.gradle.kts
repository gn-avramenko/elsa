import com.gridnine.elsa.gradle.plugin.elsa
import com.gridnine.elsa.gradle.plugin.elsaTS

System.setProperty("maven.repo.local", project.file("maven-local").absolutePath)

plugins {
    java
}

buildscript {
    repositories{
        mavenLocal()
        mavenCentral()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle:0+")
    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()
apply<com.gridnine.elsa.gradle.plugin.ElsaTsPlugin>()
elsa{

}
elsaTS {

}

task("publishToMavenLocal"){
    group = "other"
}
task("publishInternalGradlePluginToLocalMavenRepository"){
    group = "elsa"
}

task("publishGradlePluginToLocalMavenRepository"){
    group = "elsa"
}