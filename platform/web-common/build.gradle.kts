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
    packageName = "elsa-core"
    codegen {
        folder("src/ts-gen"){
            remoting("core-remoting", "../common/src/main/codegen/elsa-core-remoting.xml")
        }
    }
}
