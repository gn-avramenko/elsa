import com.gridnine.elsa.gradle.plugin.elsa

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

elsa{

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