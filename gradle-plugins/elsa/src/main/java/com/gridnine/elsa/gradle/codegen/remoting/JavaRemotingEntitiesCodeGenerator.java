/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;

import java.io.File;
import java.util.Set;

public class JavaRemotingEntitiesCodeGenerator {
    public static void generate(RemotingMetaRegistry registry, File destDir, Set<File> generatedFiles) throws Exception {
        for(EnumDescription ed : registry.getEnums().values()){
            JavaCodeGeneratorUtils.generateJavaEnumCode(ed, destDir, generatedFiles);
        }
        for(EntityDescription ed: registry.getEntities().values()){
            JavaCodeGeneratorUtils.generateJavaEntityCode(ed, destDir, generatedFiles);
        }
    }
}
