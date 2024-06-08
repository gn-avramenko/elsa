plugins {
    java
}
java.testResultsDir.set(layout.buildDirectory.dir("junit-xml"))

task("updateLocalGradlePlugins") {
    group = "elsa"
}

task("eCodeGen") {
    group = "elsa"
}


