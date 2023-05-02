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

    private String prepareName(String remotingId, String groupId, String methodId) {
        return "%s_%s_%s"
                .formatted(remotingId.replaceAll("-", "_"),
                        groupId.replaceAll("-", "_"),
                        methodId.replaceAll("-", "_"));
    }

    public void generate(RemotingMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, RemotingTypesRegistry rtr, File module, Set<File> generatedFiles, String packageName, File projectFolder, boolean skipClientGeneration, Map<String, Pair<String, String>> associations) throws Exception {
        var gen = new TypeScriptCodeGenerator(packageName, module, projectFolder, associations);

        for (String id : registry.getEnumsIds()) {
            TsCodeGeneratorUtils.generateWebEnumCode(sRegistry.getEnums().get(id), gen);
        }
        for (String id : registry.getEntitiesIds()) {
            var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry, rtr.getEntityTags());
            TsCodeGeneratorUtils.generateWebEntityCode(ged, stRegistry, gen);
        }
        if (!skipClientGeneration) {
            gen.wrapWithBlock("export const remotingClient = ", () -> {
                for (var remoting : registry.getRemotings().values()) {
                    for (var group : remoting.getGroups().values()) {
                        for (var serverCall : group.getServerCalls().values()) {
                            gen.getTsImports().add("elsa-core:ServerCallOptions");
                            gen.getTsImports().add("elsa-core:serverCall");
                            gen.blankLine();
                            if (serverCall.getRequestClassName() != null) {
                                gen.printLine("%s: (request:%s, options?:ServerCallOptions) => serverCall<%s, %s>('%s', '%s', '%s', request, options),"
                                        .formatted(prepareName(remoting.getId(), group.getId(), serverCall.getId()),
                                                JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                serverCall.getResponseClassName() == null ? "void" : JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                remoting.getId(), group.getId(), serverCall.getId()));
                            } else {
                                gen.printLine("%s: (options?:ServerCallOptions) => serverCall<void, %s>('%s', '%s', '%s', null, options),"
                                        .formatted(prepareName(remoting.getId(), group.getId(), serverCall.getId()),
                                                serverCall.getResponseClassName() != null ? JavaCodeGeneratorUtils.getSimpleName(serverCall.getResponseClassName()) : null,
                                                remoting.getId(), group.getId(), serverCall.getId()));
                            }
                        }
                        for (var subscription : group.getSubscriptions().values()) {
                            gen.blankLine();
                            gen.getTsImports().add("elsa-core:SubscriptionOptions");
                            gen.getTsImports().add("elsa-core:UnsubscriptionOptions");
                            gen.getTsImports().add("elsa-core:subscribe");
                            gen.getTsImports().add("elsa-core:unsubscribe");
                            gen.printLine("// eslint-disable-next-line no-unused-vars");
                            if (subscription.getParameterClassName() != null) {
                                if (subscription.getEventClassName() != null) {
                                    gen.printLine("%s_subscribe: (parameter:%s, handler: (ev:%s) => Promise<void>, options?:SubscriptionOptions) => subscribe<%2$s, %3$s>('%s', '%s', '%s', parameter, handler, options),"
                                            .formatted(prepareName(remoting.getId(), group.getId(), subscription.getId()),
                                                    JavaCodeGeneratorUtils.getSimpleName(subscription.getParameterClassName()),
                                                    JavaCodeGeneratorUtils.getSimpleName(subscription.getEventClassName()),
                                                    remoting.getId(), group.getId(), subscription.getId()));
                                } else {
                                    gen.printLine("%s_subscribe: (parameter:%s, handler: () => Promise<void>, options?:SubscriptionOptions) => subscribe<$2, undefined>('%s', '%s', '%s', parameter, handler, options),"
                                            .formatted(prepareName(remoting.getId(), group.getId(), subscription.getId()),
                                                    JavaCodeGeneratorUtils.getSimpleName(subscription.getParameterClassName()),
                                                    remoting.getId(), group.getId(), subscription.getId()));
                                }
                            } else {
                                if (subscription.getEventClassName() != null) {
                                    gen.printLine("%s_subscribe: (handler: (ev:%s) => Promise<void>, options?:SubscriptionOptions) => subscribe<undefined, %2$s>('%s', '%s', '%s', undefined, handler, options),"
                                            .formatted(prepareName(remoting.getId(), group.getId(), subscription.getId()),
                                                    JavaCodeGeneratorUtils.getSimpleName(subscription.getEventClassName()),
                                                    remoting.getId(), group.getId(), subscription.getId()));
                                } else {
                                    gen.printLine("%s_subscribe: (handler: () => Promise<void>, options?:SubscriptionOptions) => subscribe<undefined, undefined>('%s', '%s', '%s', undefined, handler, options),"
                                            .formatted(prepareName(remoting.getId(), group.getId(), subscription.getId()),
                                                    remoting.getId(), group.getId(), subscription.getId()));
                                }
                            }
                            gen.blankLine();
                            gen.printLine("%s_unsubscribe: (callId:string, options?:UnsubscriptionOptions) => unsubscribe('%s', '%s', '%s', callId, options),"
                                    .formatted(prepareName(remoting.getId(), group.getId(), subscription.getId()),
                                            remoting.getId(), group.getId(), subscription.getId()));
                        }
                    }
                }
            });
            gen.print(";\n");
        }
        var file = TsCodeGeneratorUtils.saveIfDiffers(gen.toString(), module);
        generatedFiles.add(file);
    }
}
