/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.codegen.domain.JavaDomainConfiguratorCodeGenerator;
import com.gridnine.elsa.gradle.parser.remoting.RemotingMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class JavaRemotingCodeGenerator implements CodeGenerator<JavaRemotingCodeGenRecord> {
    @Override
    public void generate(List<JavaRemotingCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        var configurators = new LinkedHashMap<String, List<File>>();
        records.forEach(it ->{
            BuildExceptionUtils.wrapException(() ->{
                var parser = new RemotingMetaRegistryParser();
                var metaRegistry = new RemotingMetaRegistry();
                parser.updateMetaRegistry(metaRegistry, it.getSources());
                BuildExceptionUtils.wrapException(() -> JavaRemotingConfiguratorCodeGenerator.generate(metaRegistry, it.getRegistryConfigurator(), destDir, generatedFiles));
                BuildExceptionUtils.wrapException(() -> JavaRemotingEntitiesCodeGenerator.generate(metaRegistry, destDir, generatedFiles));
            });
        });
    }
}
