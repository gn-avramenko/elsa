/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ElsaJavaConfigurationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create("elsa-java-configuration", ElsaJavaExtension.class);
    }
}
