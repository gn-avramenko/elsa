import com.gridnine.elsa.gradle.plugin.elsaTS

buildscript {
    repositories{
        mavenLocal()
        mavenCentral()
    }
    dependencies{
        classpath("com.gridnine:elsa-gradle:0+")
    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaTsPlugin>()

elsaTS {
    codegen {
        folder("src-gen"){
            remoting("core-models.ts")
        }
    }
}
