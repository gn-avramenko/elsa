/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.WebCodeGeneratorUtils;

import java.io.File;
import java.util.Set;

public class WebRemotingTypesCodeGenerator {
    public static void generate(RemotingMetaRegistry registry, File destDir, String types, Set<File> generatedFiles) throws Exception {
        var gen = new TypeScriptCodeGenerator();
        for(var en: registry.getEnums().values()){
            WebCodeGeneratorUtils.generateWebEnumCode(en, gen);
        }
        for(var ett: registry.getEntities().values()){
            WebCodeGeneratorUtils.generateWebEntityCode(ett, gen);
        }

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), types, destDir);
        generatedFiles.add(file);
    }
}
