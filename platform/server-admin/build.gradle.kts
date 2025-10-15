plugins {
    java
    id("elsa-java")
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework:spring-context:6.2.7")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:server-web-app"))
    implementation(project(":platform:server-postgres"))
    implementation(project(":submodules:webpeer:server:core"))
}

elsa {
    codegen {
        domain("src/main/java-gen", "com.gridnine.platform.elsa.admin.AdminDomainConfigurator",
            listOf("src/main/codegen/admin-domain.xml"))
        webApp("src/main/java-gen", "src/main/java","com.gridnine.platform.elsa.admin.AdminWebAppConfigurator",
            listOf("src/main/codegen/admin-web-app.xml"));
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

group ="com.gridnine.elsa"
version ="1.0"