plugins {
    java
    id( "org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.gridnine"
version = "0.0.1"

repositories{
    mavenCentral()
}


dependencies{
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


java{
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}
tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
    options.compilerArgs.addAll(arrayListOf("--release", "8"))
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    launchScript()
}


