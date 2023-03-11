/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.Set;

public class JavaDomainCodeGen {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, DomainTypesRegistry dtReg, File destDir, Set<File> generatedFiles) throws Exception {
        new JavaDomainEntitiesCodeGen().generate(registry, sRegistry, stRegistry, dtReg, destDir, generatedFiles);
        new JavaDomainCachedEntitiesCodeGen().generate(registry, sRegistry, stRegistry, dtReg, destDir, generatedFiles);
        new JavaDomainFieldsClassCodeGenerator().generate(registry, sRegistry, stRegistry, dtReg, destDir, generatedFiles);
    }
}
