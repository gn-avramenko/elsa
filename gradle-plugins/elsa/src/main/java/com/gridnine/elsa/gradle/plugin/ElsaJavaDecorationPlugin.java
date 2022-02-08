/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ElsaJavaDecorationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getPlugins().apply("java");
        target.getRepositories().add(target.getRepositories().mavenCentral());
        var ext  = target.getExtensions().getByType(ElsaJavaExtension.class);
        target.getTasks().create("eCodeGen", ElsaCodeGenTask.class);
    }
}
