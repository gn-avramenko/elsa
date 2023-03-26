/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Set;

public class RemotingJavaCodeGen {
    public void generate(RemotingMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, RemotingTypesRegistry rtr, File destDir, Set<File> generatedFiles) throws Exception {
        for(String enumId: registry.getEnumsIds()){
            JavaCodeGeneratorUtils.generateEnum(sRegistry.getEnums().get(enumId), destDir, generatedFiles);
        }
        for(String entityId: registry.getEntitiesIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(entityId, sRegistry,rtr.getEntityTags());
            JavaCodeGeneratorUtils.generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
        }
    }
}
