buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle.jar")))
    }
}

apply<com.gridnine.elsa.gradle.plugin.ElsaWebPlugin>()

configure<com.gridnine.elsa.gradle.plugin.ElsaWebExtension>{
    codegen {
        remoting("src-gen", "demo-test-types.ts",
            arrayListOf("../server/code-gen/demo-elsa-remoting.xml") )
    }
}