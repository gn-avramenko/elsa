plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(project(":submodules:webpeer:server:core"))
    implementation("org.springframework:spring-context:6.2.7")
    implementation(project(":platform:common-meta"))
    implementation(project(":platform:common-core"))
}

group ="com.gridnine.elsa"
version ="1.0"