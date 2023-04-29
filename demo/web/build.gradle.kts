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
        packageName("elsa-demo-web")
        priority(10.0);
        folder("src-gen"){
            domain("demo-domain", "../server/src/main/codegen/elsa-demo-common-domain.xml")
            remoting("demo-remoting","../server/src/main/codegen/elsa-demo-server-remoting.xml")
        }
    }
}
