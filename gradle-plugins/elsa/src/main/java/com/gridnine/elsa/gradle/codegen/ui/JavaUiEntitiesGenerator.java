/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.ui;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.ui.*;
import com.gridnine.elsa.gradle.codegen.common.GenEntityDescription;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;

import java.io.File;
import java.util.Set;

public class JavaUiEntitiesGenerator {
    public static void generate(UiMetaRegistry registry, File destDir, Set<File> generatedFiles) throws Exception {
        for(EnumDescription ed : registry.getEnums().values()){
            JavaCodeGeneratorUtils.generateJavaEnumCode(ed, destDir, generatedFiles);
        }
        for(EntityDescription ed : registry.getEntities().values()){
            JavaCodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
    }
}
