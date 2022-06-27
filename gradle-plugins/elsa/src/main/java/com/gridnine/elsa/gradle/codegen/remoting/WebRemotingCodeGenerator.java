/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.elsa.gradle.parser.remoting.RemotingMetaRegistryParser;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.*;

public class WebRemotingCodeGenerator implements CodeGenerator<WebRemotingCodeGenRecord> {
    @Override
    public void generate(List<WebRemotingCodeGenRecord> records, File destDir, Set<File> generatedFiles, Map<Object,Object> context) throws Exception{
        records.forEach(it -> {
            var parser = new RemotingMetaRegistryParser();
            var metaRegistry = new RemotingMetaRegistry();
            parser.updateMetaRegistry(metaRegistry, it.getSources());
            BuildExceptionUtils.wrapException(() -> WebRemotingTypesCodeGenerator.generate(metaRegistry,  destDir, it.getTypesDefinition(), generatedFiles));
        });
    }
}
