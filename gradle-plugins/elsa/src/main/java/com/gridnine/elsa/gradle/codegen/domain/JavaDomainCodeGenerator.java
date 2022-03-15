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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class JavaDomainCodeGenerator implements CodeGenerator<JavaDomainCodeGenRecord> {
    @Override
    public void generate(List<JavaDomainCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var configurators = new LinkedHashMap<String, List<File>>();
        records.forEach(it -> configurators.computeIfAbsent(it.getRegistryConfigurator(), (key) -> new ArrayList<>()).add(it.getSource()));
        var parser = new DomainMetaRegistryParser();
        configurators.forEach((configurator, files) -> {
            var metaRegistry = new DomainMetaRegistry();
            parser.updateMetaRegistry(metaRegistry, files);
            BuildExceptionUtils.wrapException(() ->JavaDomainConfiguratorCodeGenerator.generate(metaRegistry, configurator, destDir, generatedFiles));
            BuildExceptionUtils.wrapException(() ->JavaDomainEntitiesCodeGenerator.generate(metaRegistry, destDir, generatedFiles));
            BuildExceptionUtils.wrapException(() ->JavaDomainCachedObjectsCodeGenerator.generate(metaRegistry, (DomainMetaRegistry) context.get("domain-meta-registry"), destDir, generatedFiles));
        });
    }
}
