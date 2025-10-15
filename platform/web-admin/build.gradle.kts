import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("elsa-web")
    id("com.github.node-gradle.node") version "7.0.2"
}

elsa {
    codegen {
        webApp("src-gen", "src", arrayListOf<String>("../server-admin/src/main/codegen/admin-web-app.xml"))
    }
}

node {
    version.set("20.0.0")
    download.set(true)
    workDir.set(project.layout.buildDirectory.file("nodejs").get().asFile)
    npmWorkDir.set(project.layout.buildDirectory.file("npm").get().asFile)
}

tasks.register<NpxTask>("eslint-fix") {
    group = "elsa"
    description = "Run ESLint code analysis"

    command.set("eslint")
    args.set(listOf(
        "src/**",
        "src-gen/**",
        "--ext", ".js,.ts,.jsx,.tsx",
        "--format", "stylish",
        "--fix"
    ))

    ignoreExitValue.set(false)
}