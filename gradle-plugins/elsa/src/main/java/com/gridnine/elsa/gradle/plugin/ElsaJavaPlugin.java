/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ElsaJavaPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        var ext = target.getExtensions().create("elsa-java-configuration", ElsaJavaExtension.class);
        ext.setProjectDir(target.getProjectDir());
        target.getTasks().create("eCodeGen", ElsaCodeGenTask.class);
    }
}
