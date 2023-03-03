buildscript {
    repositories{
        mavenLocal()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle:0+")
    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaTypesExtension> {
    xsdsLocation("xsds")
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaDecorationPlugin>()


task("publishToMavenLocal"){
    group = "other"
}
task("publishInternalGradlePluginToLocalMavenRepository"){
    group = "elsa"
}

task("publishGradlePluginToLocalMavenRepository"){
    group = "elsa"
}