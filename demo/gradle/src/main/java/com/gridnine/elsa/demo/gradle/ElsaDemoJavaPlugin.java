/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.gradle;

import com.gridnine.elsa.gradle.plugin.ElsaJavaExtension;
import com.gridnine.elsa.gradle.plugin.ElsaWebExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ElsaDemoJavaPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        var ext = target.getExtensions().findByType( ElsaJavaExtension.class);
        var gridHandler = new GridContainerTemplateHandler();
        if(ext != null){
            ext.setProjectDir(target.getProjectDir());
            ext.getTemplatesHandlers().put(gridHandler.getTagName(), gridHandler);
            return;
        }
        var ext2 = target.getExtensions().findByType(ElsaWebExtension.class);
        ext2.setProjectDir(target.getProjectDir());
        ext2.getTemplatesHandlers().put(gridHandler.getTagName(), gridHandler);
    }
}
