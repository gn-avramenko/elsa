import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.npm.task.NpxTask
import org.gradle.kotlin.dsl.register

plugins {
    id("elsa-web")
    id("com.github.node-gradle.node") version "7.0.2"
}

elsa {
    codegen {
        webApp("src-gen", "src", "registry.AdminConfigurator.ts", "com.gridnine.platform.elsa.admin.web", "admin", true, arrayListOf<String>("../server-admin/src/main/codegen/common.xml",
            "../server-admin/src/main/codegen/main-frame.xml",
            "../server-admin/src/main/codegen/entity-editor.xml","../server-admin/src/main/codegen/form.xml",
            "../server-admin/src/main/codegen/grid.xml",
            "../server-admin/src/main/codegen/acl.xml",
            "../server-admin/src/main/codegen/entity-list.xml"))
    }
}

node {
    version.set("22.18.0")
    download.set(true)
    workDir.set(project.layout.buildDirectory.file("nodejs").get().asFile)
    npmWorkDir.set(project.layout.buildDirectory.file("npm").get().asFile)
}

tasks.register<NpmTask>("install-dependencies") {
    group = "other"
    description = "Install dependencies"

    args.set(listOf("install"))
    ignoreExitValue.set(false)
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