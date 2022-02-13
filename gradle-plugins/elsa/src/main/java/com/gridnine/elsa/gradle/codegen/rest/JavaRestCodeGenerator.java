/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.rest;

import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.parser.rest.RestMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class JavaRestCodeGenerator implements CodeGenerator<JavaRestCodeGenRecord> {
    @Override
    public void generate(List<JavaRestCodeGenRecord> records, File destDir, Set<File> generatedFiles) throws Exception{
        var configurators = new LinkedHashMap<String, List<File>>();
        records.forEach(it -> configurators.computeIfAbsent(it.getRegistryConfigurator(), (key) -> new ArrayList<>()).add(it.getSource()));
        var parser = new RestMetaRegistryParser();
        configurators.forEach((configurator, files) -> {
            var metaRegistry = new RestMetaRegistry(Collections.emptyList());
            parser.updateMetaRegistry(metaRegistry, files);
            BuildExceptionUtils.wrapException(() -> JavaRestConfiguratorCodeGenerator.generate(metaRegistry, configurator, destDir, generatedFiles));
            BuildExceptionUtils.wrapException(() -> JavaRestEntitiesCodeGenerator.generate(metaRegistry,  destDir, generatedFiles));
        });
    }
}
