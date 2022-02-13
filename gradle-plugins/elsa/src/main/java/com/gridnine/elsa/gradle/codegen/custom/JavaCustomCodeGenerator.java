/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.custom;

import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.rest.JavaRestEntitiesCodeGenerator;
import com.gridnine.elsa.gradle.parser.custom.CustomMetaRegistryParser;
import com.gridnine.elsa.gradle.parser.rest.RestMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class JavaCustomCodeGenerator implements CodeGenerator<JavaCustomCodeGenRecord> {
    @Override
    public void generate(List<JavaCustomCodeGenRecord> records, File destDir, Set<File> generatedFiles) throws Exception{
        var configurators = new LinkedHashMap<String, List<File>>();
        records.forEach(it -> configurators.computeIfAbsent(it.getRegistryConfigurator(), (key) -> new ArrayList<>()).add(it.getSource()));
        var parser = new CustomMetaRegistryParser();
        configurators.forEach((configurator, files) -> {
            var metaRegistry = new CustomMetaRegistry(Collections.emptyList());
            parser.updateMetaRegistry(metaRegistry, files);
            BuildExceptionUtils.wrapException(() -> JavaCustomConfiguratorCodeGenerator.generate(metaRegistry, configurator, destDir, generatedFiles));
        });
    }
}
