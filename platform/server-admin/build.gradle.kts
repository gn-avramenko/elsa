plugins {
    java
    id("java-test-fixtures")
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("elsa-java")
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.springframework:spring-context:6.2.7")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation(project(":common-meta"))
    implementation(project(":common-core"))
    implementation(project(":server-core"))
    implementation(project(":server-web-app"))
    implementation(project(":webpeer-server-core"))
    testFixturesImplementation(testFixtures(project(":common-core")))
    testFixturesImplementation(testFixtures(project(":server-core")))
    testFixturesImplementation(testFixtures(project(":server-atomikos")))
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
}

elsa {
    codegen {
        l10n("src/main/java-gen", "com.gridnine.platform.elsa.admin.AdminL10nConfigurator",
            "com.gridnine.platform.elsa.admin.AdminL10nFactory", listOf("src/main/codegen/admin-l10n-messages.xml"))
        domain("src/main/java-gen", "com.gridnine.platform.elsa.admin.AdminDomainConfigurator",
            listOf("src/main/codegen/admin-domain.xml"))
        webApp("src/main/java-gen", "src/main/java","com.gridnine.platform.elsa.admin.AdminWebAppConfigurator",
            true, listOf("src/main/codegen/common.xml","src/main/codegen/main-frame.xml",
                "src/main/codegen/entity-editor.xml","src/main/codegen/entity-list.xml", "src/main/codegen/form.xml"))
    }
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}
sourceSets.testFixtures {
    java.srcDirs("src/testFixtures/java-gen")
}
group ="com.gridnine.elsa"
version ="1.0"