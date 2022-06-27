/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.custom;

import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainCachedObjectsCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainConfiguratorCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainEntitiesCodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainFieldsClassCodeGenerator;
import com.gridnine.elsa.gradle.parser.custom.CustomMetaRegistryParser;
import com.gridnine.elsa.gradle.parser.domain.DomainMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class JavaCustomCodeGenerator implements CodeGenerator<JavaCustomCodeGenRecord> {
    @Override
    public void generate(List<JavaCustomCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var parser = new CustomMetaRegistryParser();
                var metaRegistry = new CustomMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                BuildExceptionUtils.wrapException(() -> JavaCustomConfiguratorCodeGenerator.generate(metaRegistry, it.getRegistryConfigurator(), destDir, generatedFiles));
            });
        });
    }
}
