plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.0.11")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
}


sourceSets.main {
    java.srcDirs("src/main/java")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    failFast = true
}
