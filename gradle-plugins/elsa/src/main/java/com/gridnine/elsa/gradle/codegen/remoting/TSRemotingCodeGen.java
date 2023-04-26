/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TsCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import kotlin.Pair;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class TSRemotingCodeGen {

    private String prepareName(String remotingId, String groupId, String methodId){
        return "%s_%s_%s"
                .formatted(remotingId.replaceAll("-", "_"),
                        groupId.replaceAll("-", "_"),
                        methodId.replaceAll("-", "_"));
    }
    public void generate(RemotingMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, RemotingTypesRegistry rtr, File module, Set<File> generatedFiles, String packageName, File projectFolder, boolean skipClientGeneration, Map<String, Pair<String,String>> associations) throws Exception {
        var gen = new TypeScriptCodeGenerator(packageName, module, projectFolder, associations);
        gen.getTsImports().add("elsa-core:ServerCallOptions");
        for(String id: registry.getEnumsIds()){
            TsCodeGeneratorUtils.generateWebEnumCode(sRegistry.getEnums().get(id), gen);
        }
        for(String id: registry.getEntitiesIds()){
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        if(!skipClientGeneration){
            gen.wrapWithBlock("export const remotingClient = ", () ->{
                for(var remoting: registry.getRemotings().values()){
                    for(var group: remoting.getGroups().values()){
                        for(var serverCall: group.getServerCalls().values()){
                            gen.blankLine();
                            if(serverCall.getRequestClassName() != null){
                                gen.printLine("%s: (request:%s, options?:ServerCallOptions) => serverCall<%s, %s>('%s', '%s', '%s', request, options),"
                                        .formatted(prepareName(remoting.getId(), group.getId(), serverCall.getId()),
                                                JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                serverCall.getResponseClassName() == null ? "void" : JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                remoting.getId(), group.getId(), serverCall.getId()));
                            } else {
                                gen.printLine("%s: (options?:ServerCallOptions) => serverCall<void, %s>('%s', '%s', '%s', null, options),"
                                        .formatted(prepareName(remoting.getId(), group.getId(), serverCall.getId()),
                                                serverCall.getResponseClassName() != null ? JavaCodeGeneratorUtils.getSimpleName(serverCall.getResponseClassName()): null,
                                                remoting.getId(), group.getId(), serverCall.getId()));
                            }
                        }
                    }
                }
            });
        }
        var file =TsCodeGeneratorUtils.saveIfDiffers(gen.toString(), module);
        generatedFiles.add(file);
    }
}
