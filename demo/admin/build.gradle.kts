
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
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:server-web-app"))
    implementation(project(":platform:server-admin"))
    implementation(project(":platform:server-postgres"))
    implementation(project(":submodules:webpeer:server:core"))
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}


elsa {
    codegen {
        domain("src/main/java-gen", "com.gridnine.platform.elsa.demo.admin.DemoAdminDomainConfigurator",
            listOf("src/main/codegen/demo-admin-domain.xml"))
    }
}