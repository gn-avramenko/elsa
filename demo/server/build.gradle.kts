plugins{
    java
    id( "org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}
buildscript {
    dependencies{
        classpath(files(File(projectDir.parentFile.parentFile, "gradle/elsa-gradle.jar")))
    }
}
apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

repositories {
    mavenCentral()
}
configure<com.gridnine.elsa.gradle.plugin.ElsaJavaExtension>{
    codegen {
        domain("code-gen/demo-elsa-domain.xml",
            "src/main/java-gen",
            "com.gridnine.elsa.demo.DemoElsaDomainMetaRegistryConfigurator")
    }
}




dependencies{
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:server-postgres"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sourceSets.main{
    java.srcDirs("src/main/java","src/main/java-gen")
}

//plugins {
//    java
//    id( "org.springframework.boot") version "2.6.0"
//    id("io.spring.dependency-management") version "1.0.11.RELEASE"
//}
//
//group = "com.gridnine"
//version = "0.0.1"
//
//repositories{
//    mavenCentral()
//}
//
//
//dependencies{
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation(project(":platform:common-meta"))
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//}
//
//
//java{
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_1_8
//    withSourcesJar()
//}
//tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
//    options.compilerArgs.addAll(arrayListOf("--release", "8"))
//}
//
//tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//    launchScript()
//}
//
//
