plugins {
    java
}
repositories {
    mavenCentral()
}

group ="com.gridnine.elsa"
version ="1.0"

dependencies {
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("com.mchange:c3p0:0.9.5.5")
    implementation("org.springframework:spring-jdbc:6.2.7")
    implementation("org.springframework:spring-context:6.2.7")
    implementation(project(":server-core"))
    implementation(project(":common-core"))
}
