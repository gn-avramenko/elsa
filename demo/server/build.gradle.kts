plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.gridnine.elsa.demo"
version = ""

springBoot {
    mainClass.set("com.gridnine.elsa.demo.app.AdminUiServer")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation(project(":platform:common-core"))
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:server-core"))
    implementation(project(":platform:server-postgres"))
    implementation(project(":platform:server-admin"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
