/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.remoting;

import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingDownloadDescription;
import com.gridnine.elsa.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.remoting.RemotingUploadDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.Set;

public class RemotingJavaMetaRegistryConfiguratorCodeGenerator {
    public void generate(RemotingMetaRegistry registry, SerializableMetaRegistry sRegistry, String configurator, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(configurator));
        gen.addImport("com.gridnine.elsa.meta.remoting.RemotingMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.serialization.SerializableMetaRegistry");
        gen.addImport("com.gridnine.elsa.meta.config.Environment");

        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(configurator)), () -> {
            gen.blankLine();
            gen.wrapWithBlock("public void configure()", () -> {
                gen.printLine("var smr = Environment.getPublished(SerializableMetaRegistry.class);");
                gen.printLine("var rmr = Environment.getPublished(RemotingMetaRegistry.class);");
                for (String enumId : registry.getEnumsIds()) {
                    var ed = sRegistry.getEnums().get(enumId);
                    JavaCodeGeneratorUtils.generateEnumMetaRegistryConfiguratorCode(ed, "smr", "rmr", gen);
                }
                for (String entityId : registry.getEntitiesIds()) {
                    var ed = sRegistry.getEntities().get(entityId);
                    JavaCodeGeneratorUtils.generateEntityMetaRegistryConfiguratorCode(ed, "smr", "rmr.getEntitiesIds()", gen);
                }
                for (RemotingDescription remoting : registry.getRemotings().values()) {
                    gen.wrapWithBlock("", () -> {
                        gen.addImport("com.gridnine.elsa.meta.remoting.RemotingDescription");
                        gen.printLine("var remotingDescription = new RemotingDescription(\"%s\");".formatted(remoting.getId()));
                        JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(remoting, "remotingDescription", gen);
                        gen.printLine("rmr.getRemotings().put(\"%s\", remotingDescription);".formatted(remoting.getId()));
                        for (RemotingGroupDescription group : remoting.getGroups().values()) {
                            gen.wrapWithBlock("", () -> {
                                gen.addImport("com.gridnine.elsa.meta.remoting.RemotingGroupDescription");
                                gen.printLine("var groupDescription = new RemotingGroupDescription(\"%s\");".formatted(group.getId()));
                                JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(group, "groupDescription", gen);
                                gen.printLine("remotingDescription.getGroups().put(\"%s\", groupDescription);".formatted(group.getId()));
                                for(RemotingServerCallDescription serverCall: group.getServerCalls().values()){
                                    gen.wrapWithBlock("", () -> {
                                        gen.addImport("com.gridnine.elsa.meta.remoting.RemotingServerCallDescription");
                                        gen.printLine("var serverCallDescription = new RemotingServerCallDescription(\"%s\");".formatted(serverCall.getId()));
                                        JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(serverCall, "serverCallDescription", gen);
                                        if(serverCall.getRequestClassName() != null){
                                            gen.printLine("serverCallDescription.setRequestClassName(\"%s\");".formatted(serverCall.getRequestClassName()));
                                        }
                                        if(serverCall.getResponseClassName() != null){
                                            gen.printLine("serverCallDescription.setResponseClassName(\"%s\");".formatted(serverCall.getResponseClassName()));
                                        }
                                        gen.printLine("groupDescription.getServerCalls().put(\"%s\", serverCallDescription);".formatted(serverCall.getId()));
                                    });
                                }
                                for(RemotingDownloadDescription download: group.getDownloads().values()){
                                    gen.wrapWithBlock("", () -> {
                                        gen.addImport("com.gridnine.elsa.meta.remoting.RemotingDownloadDescription");
                                        gen.printLine("var downloadDescription = new RemotingDownloadDescription(\"%s\");".formatted(download.getId()));
                                        JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(download, "downloadDescription", gen);
                                        if(download.getRequestClassName() != null){
                                            gen.printLine("downloadDescription.setRequestClassName(\"%s\");".formatted(download.getRequestClassName()));
                                        }
                                        gen.printLine("groupDescription.getDownloads().put(\"%s\", downloadDescription);".formatted(download.getId()));
                                    });
                                }
                                for(RemotingUploadDescription upload: group.getUploads().values()){
                                    gen.wrapWithBlock("", () -> {
                                        gen.addImport("com.gridnine.elsa.meta.remoting.RemotingUploadDescription");
                                        gen.printLine("var uploadDescription = new RemotingUploadDescription(\"%s\");".formatted(upload.getId()));
                                        JavaCodeGeneratorUtils.generateBaseElementMetaRegistryConfiguratorCode(upload, "uploadDescription", gen);
                                        if(upload.getRequestClassName() != null){
                                            gen.printLine("uploadDescription.setRequestClassName(\"%s\");".formatted(upload.getRequestClassName()));
                                        }
                                        gen.printLine("groupDescription.getUploads().put(\"%s\", uploadDescription);".formatted(upload.getId()));
                                    });
                                }

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
