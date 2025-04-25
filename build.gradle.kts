plugins {
    java
}
java.testResultsDir.set(layout.buildDirectory.dir("junit-xml"))

task("updateLocalGradlePlugins") {
    group = "elsa"
}
//hello
task("eCodeGen") {
    group = "elsa"
}


