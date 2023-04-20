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
        packageName("elsa-demo-web")
        folder("src-gen"){
            domain("demo-domain", "../server/src/main/codegen/elsa-demo-common-domain.xml")
        }
    }
}
