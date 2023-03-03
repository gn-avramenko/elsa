/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.codegen.ElsaCodeGenTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ElsaJavaDecorationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getTasks().create("eCodeGen", ElsaCodeGenTask.class);
    }
}
