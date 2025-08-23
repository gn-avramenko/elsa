plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(project(":submodules:webpeer:server:core"))
}

group ="com.gridnine.elsa"
version ="1.0"