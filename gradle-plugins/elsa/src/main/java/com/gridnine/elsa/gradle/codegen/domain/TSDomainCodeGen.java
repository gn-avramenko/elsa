/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TsCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import kotlin.Pair;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class TSDomainCodeGen {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, DomainTypesRegistry rtr, File module, Set<File> generatedFiles, String packageName, Map<String, Pair<String,File>> associations) throws Exception {
        var gen = new TypeScriptCodeGenerator(packageName, module, associations);
        for(String id: registry.getEnumsIds()){
            TsCodeGeneratorUtils.generateWebEnumCode(sRegistry.getEnums().get(id), gen);
        }
        for(String id: registry.getEntitiesIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        for(String id: registry.getAssetsIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        for(String id: registry.getDocumentsIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        for(String id: registry.getProjectionsIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        var file =TsCodeGeneratorUtils.saveIfDiffers(gen.toString(), module);
        generatedFiles.add(file);
    }
}
