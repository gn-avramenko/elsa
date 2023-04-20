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
        packageName("elsa-web-common")
        folder("src-gen"){
            remoting("core-remoting", "../common/src/main/codegen/elsa-core-remoting.xml")
        }
    }
}
