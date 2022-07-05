/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.Set;

public class JavaRemotingConfiguratorCodeGenerator {
    public static void generate(RemotingMetaRegistry registry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistryConfigurator");
        gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry");
        gen.wrapWithBlock("public class %s implements RemotingMetaRegistryConfigurator".formatted(JavaCodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.printLine("@Override");
            gen.wrapWithBlock("public void updateMetaRegistry(RemotingMetaRegistry registry)", () -> {
                for (EnumDescription ed : registry.getEnums().values()) {
                    JavaCodeGeneratorUtils.generateJavaEnumConfiguratorCode(ed, gen);
                }
                for (EntityDescription ed : registry.getEntities().values()) {
                    JavaCodeGeneratorUtils.generateJavaEntityConfiguratorCode(ed, gen);
                }
                for(RemotingDescription rd: registry.getRemotings().values()){
                    gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingDescription");
                    gen.wrapWithBlock(null, () -> {
                        gen.printLine("var remotingDescription = new RemotingDescription(\"%s\");".formatted(rd.getId()));
                        gen.printLine("registry.getRemotings().put(remotingDescription.getId(), remotingDescription);");
                        for (RemotingGroupDescription gd : rd.getGroups().values()) {
                            gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingGroupDescription");
                            gen.wrapWithBlock(null, () -> {
                                gen.printLine("var groupDescription = new RemotingGroupDescription(\"%s\");".formatted(gd.getId()));
                                gen.printLine("remotingDescription.getGroups().put(groupDescription.getId(), groupDescription);");
                                gd.getServerCalls().values().forEach(sc -> {
                                    gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingServerCallDescription");
                                    BuildExceptionUtils.wrapException(() -> {
                                        gen.wrapWithBlock(null, () -> {
                                            gen.printLine("var serverCallDescription = new RemotingServerCallDescription(\"%s\");".formatted(gd.getId()));
                                            gen.printLine("serverCallDescription.setValidatable(%s);".formatted(sc.isValidatable()));
                                            if(sc.getRequestClassName() != null) {
                                                gen.printLine("serverCallDescription.setRequestClassName(\"%s\");".formatted(sc.getRequestClassName()));
                                            }
                                            if(sc.getResponseClassName() != null) {
                                                gen.printLine("serverCallDescription.setResponseClassName(\"%s\");".formatted(sc.getResponseClassName()));
                                            }
                                            gen.printLine("groupDescription.getServerCalls().put(\"%s\", serverCallDescription);".formatted(sc.getId()));
                                        });
                                    });

                                });
                                gd.getClientCalls().values().forEach(cc -> {
                                    gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingClientCallDescription");
                                    BuildExceptionUtils.wrapException(() -> {
                                        gen.wrapWithBlock(null, () -> {
                                            gen.printLine("var clientCallDescription = new RemotingClientCallDescription(\"%s\");".formatted(cc.getId()));
                                            gen.printLine("clientCallDescription.setRequestClassName(\"%s\");".formatted(cc.getRequestClassName()));
                                            gen.printLine("clientCallDescription.setResponseClassName(\"%s\");".formatted(cc.getResponseClassName()));
                                            gen.printLine("groupDescription.getClientCalls().put(\"%s\", clientCallDescription);".formatted(cc.getId()));
                                        });
                                    });

                                });
                                gd.getSubscriptions().values().forEach(sub -> {
                                    gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingSubscriptionDescription");
                                    BuildExceptionUtils.wrapException(() -> {
                                        gen.wrapWithBlock(null, () -> {
                                            gen.printLine("var subscriptionDescription = new RemotingSubscriptionDescription(\"%s\");".formatted(sub.getId()));
                                            gen.printLine("subscriptionDescription.setParameterClassName(\"%s\");".formatted(sub.getParameterClassName()));
                                            gen.printLine("subscriptionDescription.setEventClassName(\"%s\");".formatted(sub.getEventClassName()));
                                            gen.printLine("groupDescription.getSubscriptions().put(\"%s\", subscriptionDescription);".formatted(sub.getId()));
                                        });
                                    });
                                });
                            });
                        }
                    });
                }


            });

        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator + ".java", destDir);
        generatedFiles.add(file);
    }
}
