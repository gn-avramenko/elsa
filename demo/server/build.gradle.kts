plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":platform:java-meta"))
    implementation(project(":platform:java-core"))
    implementation(project(":platform:java-server"))
    implementation(project(":platform:java-server-postgres"))
    implementation(project(":platform:sjl"))
}
