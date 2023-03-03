/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.plugin;

import com.gridnine.elsa.gradle.config.ElsaTypesExtensionData;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;

public class ElsaJavaConfigurationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        ExtraPropertiesExtension epe = target.getRootProject().getExtensions().findByType(ExtraPropertiesExtension.class);
        String key = "elsa-types-extension-data";
        assert epe != null;
        if(!epe.has(key)){
            epe.set(key, new ElsaTypesExtensionData());
        }
        ElsaTypesExtensionData data = (ElsaTypesExtensionData) epe.get(key);
        target.getExtensions().create("elsa-types-configuration", ElsaTypesExtension.class, target, data);
    }
}
