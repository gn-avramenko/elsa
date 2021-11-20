package com.gridnine.elsa.gradle.platform;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.compile.JavaCompile;

import java.util.Arrays;

public class ElsaPlatformConfigurationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getPlugins().apply(JavaPlugin.class);
        target.getPlugins().apply("java-library");
        target.getPlugins().apply("maven-publish");
        target.setProperty("group", "com.gridnine");
        target.setProperty("version", "0.0.1");
        target.getExtensions().configure("java", (JavaPluginExtension ext) ->{
            ext.setSourceCompatibility(JavaVersion.VERSION_17);
            ext.setTargetCompatibility(JavaVersion.VERSION_1_8);
            ext.withSourcesJar();
        });
        target.getTasks().withType(JavaCompile.class, (task) ->{
           task.getOptions().getCompilerArgs().addAll(Arrays.asList("--release", "8"));
        });
    }
}
