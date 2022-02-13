/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.rest;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.rest.RestDescription;
import com.gridnine.elsa.common.meta.rest.RestGroupDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestOperationDescription;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.GenEntityDescription;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;

import java.io.File;
import java.util.Set;

public class JavaRestEntitiesCodeGenerator {
    public static void generate(RestMetaRegistry registry, File destDir, Set<File> generatedFiles) throws Exception {
        for(EnumDescription ed : registry.getEnums().values()){
            CodeGeneratorUtils.generateJavaEnumCode(ed, destDir, generatedFiles);
        }
        for(EntityDescription ed: registry.getEntities().values()){
            CodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
    }

}
