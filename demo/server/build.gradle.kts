plugins {
    java
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}
buildscript {
    dependencies {
        classpath(
            files(
                File(projectDir.parentFile.parentFile, "gradle/elsa-gradle.jar"),
                File(projectDir.parentFile, "gradle/dist/elsa-gradle-demo.jar")
            )
        )
    }
}
apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

repositories {
    mavenCentral()
}
configure<com.gridnine.elsa.gradle.plugin.ElsaJavaExtension> {
    codegen {
        domain(
            "src/main/java-gen",
            "com.gridnine.elsa.demo.DemoElsaDomainMetaRegistryConfigurator",
            arrayListOf("code-gen/demo-elsa-domain.xml")
        )
        remoting(
            "src/main/java-gen", "com.gridnine.elsa.demo.DemoElsaRemotingMetaRegistryConfigurator",
            arrayListOf("code-gen/demo-elsa-remoting.xml")
        )
        uiTemplate(
            "src/main/java-gen", "elsa-demo-ui.xsd", "http://gridnine.com/elsa/demo-ui-template",
            arrayListOf("code-gen/demo-elsa-ui-template.xml")
        )
        ui("src/main/java-gen", arrayListOf("code-gen/demo-elsa-ui.xml","code-gen/demo-elsa-ui2.xml"))
    }
}

apply<com.gridnine.elsa.demo.gradle.ElsaDemoJavaPlugin>()

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-websocket")
    implementation("org.springframework:spring-messaging")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.boot:spring-boot-starter-security");
    implementation("org.springframework:spring-webflux")

    implementation("com.fasterxml.jackson.core:jackson-core:2+")

    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:server-postgres"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
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

