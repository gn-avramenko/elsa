import com.gridnine.elsa.gradle.plugin.elsa

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

elsa {
    types {
        xsdsLocation("xsds")
    }
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