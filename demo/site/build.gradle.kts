buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle.jar"),
            java.io.File(projectDir.parentFile, "gradle/dist/elsa-gradle-demo.jar")
        ))

    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaWebPlugin>()

apply<com.gridnine.elsa.demo.gradle.ElsaDemoJavaPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaWebExtension>{
    codegen {
        declareImport("src/feature/widgets.ts", "TextBoxWidget", "BigDecimalBoxWidget", "GridLayoutContainer")
        remoting("src-gen", "demo-test-types.ts",
            arrayListOf("../server/code-gen/demo-elsa-remoting.xml") )
        uiTemplate("src-gen", "demo-ui-template.ts",
            arrayListOf("../server/code-gen/demo-elsa-ui-template.xml"))
        ui("src-gen", "demo-ui.ts",
            arrayListOf("../server/code-gen/demo-elsa-ui.xml"))
        ui("src-gen", "demo-ui2.ts",
            arrayListOf("../server/code-gen/demo-elsa-ui2.xml"))
    }
}