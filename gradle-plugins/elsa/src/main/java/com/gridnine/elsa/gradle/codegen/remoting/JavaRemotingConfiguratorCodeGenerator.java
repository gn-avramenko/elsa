/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
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
                for (RemotingGroupDescription gd : registry.getGroups().values()) {
                    gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingGroupDescription");
                    gen.wrapWithBlock(null, () -> {
                        gen.printLine("var groupDescription = new RemotingGroupDescription(\"%s\");".formatted(gd.getId()));
                        gen.printLine("registry.getGroups().put(groupDescription.getId(), groupDescription);");
                        gd.getServerCalls().values().forEach(sc -> {
                            gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingServerCallDescription");
                            BuildExceptionUtils.wrapException(() -> {
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var serverCallDescription = new RemotingServerCallDescription(\"%s\");".formatted(gd.getId()));
                                    gen.printLine("serverCallDescription.setValidatable(%s);".formatted(sc.isValidatable()));
                                    gen.printLine("serverCallDescription.setRequestClassName(\"%s\");".formatted(sc.getRequestClassName()));
                                    gen.printLine("serverCallDescription.setResponseClassName(\"%s\");".formatted(sc.getResponseClassName()));
                                });
                            });

                        });
                        gd.getClientCalls().values().forEach(cc -> {
                            gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingClientCallDescription");
                            BuildExceptionUtils.wrapException(() -> {
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var clientCallDescription = new RemotingclientCallDescription(\"%s\");".formatted(gd.getId()));
                                    gen.printLine("clientCallDescription.setRequestClassName(\"%s\");".formatted(cc.getRequestClassName()));
                                    gen.printLine("clientCallDescription.setResponseClassName(\"%s\");".formatted(cc.getResponseClassName()));
                                });
                            });

                        });
                        gd.getSubscriptions().values().forEach(sub -> {
                            gen.addImport("com.gridnine.elsa.common.meta.remoting.RemotingSubscriptionDescription");
                            BuildExceptionUtils.wrapException(() -> {
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var subscriptionDescription = new RemotingSubscriptionDescription(\"%s\");".formatted(gd.getId()));
                                    gen.printLine("subscriptionDescription.setParameterClassName(\"%s\");".formatted(sub.getParameterClassName()));
                                    gen.printLine("subscriptionDescription.setEventClassName(\"%s\");".formatted(sub.getEventClassName()));
                                });
                            });
                        });
                    });
                }

            });

        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), configurator + ".java", destDir);
        generatedFiles.add(file);
    }
}
