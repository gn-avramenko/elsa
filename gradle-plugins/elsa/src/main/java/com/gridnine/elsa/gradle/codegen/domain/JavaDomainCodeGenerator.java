/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.parser.domain.DomainMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaDomainCodeGenerator implements CodeGenerator<JavaDomainCodeGenRecord> {
    @Override
    public void generate(List<JavaDomainCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var parser = new DomainMetaRegistryParser();
                var metaRegistry = new DomainMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                BuildExceptionUtils.wrapException(() ->JavaDomainConfiguratorCodeGenerator.generate(metaRegistry, it.getRegistryConfigurator(), destDir, generatedFiles));
                BuildExceptionUtils.wrapException(() ->JavaDomainEntitiesCodeGenerator.generate(metaRegistry, destDir, generatedFiles));
                BuildExceptionUtils.wrapException(() ->JavaDomainCachedObjectsCodeGenerator.generate(metaRegistry, (DomainMetaRegistry) context.get("domain-meta-registry"), destDir, generatedFiles));
                BuildExceptionUtils.wrapException(() ->JavaDomainFieldsClassCodeGenerator.generate(metaRegistry, destDir, generatedFiles));
            });
        });
    }
}
