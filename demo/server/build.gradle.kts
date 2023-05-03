import com.gridnine.elsa.gradle.plugin.elsa

buildscript {
    dependencies{
        classpath(files(project.file("../../gradle/elsa-gradle-internal.jar")))
        classpath(files(project.file("../../gradle/elsa-gradle.jar")))
    }
}


repositories {
    mavenCentral()
}

plugins {
    java
}

apply<com.gridnine.elsa.gradle.plugin.ElsaJavaPlugin>()

dependencies {
    implementation(project(":platform:meta"))
    implementation(project(":platform:common"))
    implementation(project(":platform:server"))
    implementation(project(":platform:server-postgres"))
    implementation(project(":platform:server-tomcat"))
    implementation(project(":platform:sjl"))
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/java-gen")
}

elsa {
    codegen {
        folder("src/main/java-gen"){
            domainMetaRegistryConfigurator("com.gridnine.elsa.demo.ElsaDemoDomainMetaRegistryConfigurator")
            remotingMetaRegistryConfigurator("com.gridnine.elsa.demo.ElsaDemoRemotingMetaRegistryConfigurator")
            l10nMetaRegistryConfigurator("com.gridnine.elsa.demo.ElsaDemoL10nMetaRegistryConfigurator")
            domainMeta("src/main/codegen/elsa-demo-common-domain.xml",
                "src/main/codegen/elsa-demo-server-domain.xml")
            remotingMeta("src/main/codegen/elsa-demo-server-remoting.xml"){
                subscriptionClientClassName = "com.gridnine.elsa.demo.model.remoting.ElsaDemoSubscriptionClient"
            }
            l10nMeta(null, "src/main/codegen/elsa-demo-l10n.xml")
        }
    }
}
