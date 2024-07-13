buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/gradle-plugin.jar")))
    }
}
apply<com.gridnine.platform.elsa.gradle.plugin.ElsaWebPlugin>()

configure<com.gridnine.platform.elsa.gradle.plugin.ElsaWebExtension> {
    codegen{
        remoting("src/generated", arrayListOf("../server/src/main/codegen/demo-remoting.xml"))
        l10n("src/generated", "../../platform/server-core/src/main/codegen/core-server-l10n-messages.xml")
    }
}