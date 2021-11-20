package com.gridnine.elsa.gradle.platform;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.compile.JavaCompile;

import java.util.Arrays;

public class ElsaPlatformExtensionPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create("elsa", ElsaPlatformExtension.class);
    }
}
