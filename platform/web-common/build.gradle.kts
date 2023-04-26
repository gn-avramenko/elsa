import com.gridnine.elsa.gradle.plugin.ElsaTsPlugin
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

apply<ElsaTsPlugin>()

elsaTS {
    codegen {
        packageName("elsa-core")
        priority(0.0)
        folder("src", true){
            custom("core-model-entities", "../common/src/main/codegen/elsa-core-custom.xml")
        }
        folder("src-gen"){
            remoting {
                module("core-remoting")
                skipClientGeneration(true)
                sources("../common/src/main/codegen/elsa-core-remoting.xml")
            }
        }
    }
}
