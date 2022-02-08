buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle-internal.jar")))
    }
}
apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaConfigurationPlugin>()

configure<com.gridnine.elsa.gradle.internal.ElsaInternalJavaExtension>{
    artefactId = "elsa-common-meta"
}

apply<com.gridnine.elsa.gradle.internal.ElsaInternalJavaDecorationPlugin>()

dependencies{
    "implementation"("org.springframework:spring-context:5.3.0")
}
