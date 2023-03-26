plugins {
    java
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
    implementation(project(":platform:server-postgres"))
    implementation(project(":platform:sjl"))
}
