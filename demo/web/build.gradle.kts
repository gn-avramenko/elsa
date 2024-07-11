import com.gridnine.elsa.gradle.plugin.ElsaTsPlugin
import com.gridnine.elsa.gradle.plugin.elsaTS

buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
        classpath(files(project.file("../../gradle/elsa-gradle.jar")))
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
