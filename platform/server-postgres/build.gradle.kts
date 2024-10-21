plugins {
    java
}
buildscript {
    dependencies {
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/gradle-plugin.jar")))
    }
}
repositories {
    mavenCentral()
}

apply<com.gridnine.platform.elsa.gradle.plugin.ElsaJavaPlugin>()
tasks.compileJava.get().dependsOn(tasks.getByName("eCodeGen"))


group ="com.gridnine.elsa"
version ="1.0"

dependencies {
    implementation("org.postgresql:postgresql:42.3.7")
    implementation("com.mchange:c3p0:0.9.5.5")
    implementation("org.springframework:spring-jdbc:6.0.11")
    implementation("org.springframework:spring-context:6.0.11")
    implementation(project(":platform:server-core"))
    implementation(project(":platform:common-core"))
}
