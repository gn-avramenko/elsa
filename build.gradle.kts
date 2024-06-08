plugins {
    java
}
java.testResultsDir.set(layout.buildDirectory.dir("junit-xml"))

task("updateLocalGradlePlugins") {
    group = "ldocs"
}

task("eCodeGen") {
    group = "ldocs"
}

tasks.register("showDirs") {
    val rootDir = project.rootDir
    val reportsDir = project.reporting.baseDirectory
    val testResultsDir = project.java.testResultsDir

    doLast {
        logger.quiet(rootDir.toPath().relativize(reportsDir.get().asFile.toPath()).toString())
        logger.quiet(rootDir.toPath().relativize(testResultsDir.get().asFile.toPath()).toString())
    }
}
