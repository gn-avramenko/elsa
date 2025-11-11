
plugins {
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    java
    id("elsa-java")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(project(":common-meta"))
    implementation(project(":common-core"))
    implementation(project(":server-core"))
    implementation(project(":server-web-app"))
    implementation(project(":server-postgres"))
    implementation(project(":submodules:webpeer:server:core"))
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}


elsa {
    codegen {
        domain("src/main/java-gen", "com.gridnine.platform.elsa.demo.SimpleWebAppDomainConfigurator",
            listOf("src/main/codegen/domain.xml"))
        webApp("src/main/java-gen", "src/main/java","com.gridnine.platform.elsa.demo.SimpleWebAppConfigurator", false,
            listOf("src/main/codegen/simple-web-app.xml"));
    }
}