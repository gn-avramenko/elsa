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
import com.gridnine.elsa.meta.remoting.RemotingSubscriptionDescription;
import com.gridnine.elsa.meta.remoting.RemotingUploadDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

import java.io.File;
import java.util.Set;

public class RemotingJavaSubscriptionsClientGenerator {
    public void generate(RemotingMetaRegistry registry, SerializableMetaRegistry sRegistry, String clientClassName, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(JavaCodeGeneratorUtils.getPackage(clientClassName));
        gen.addImport("com.gridnine.elsa.server.remoting.RemotingChannels");
        gen.wrapWithBlock("public class %s".formatted(JavaCodeGeneratorUtils.getSimpleName(clientClassName)), () -> {
            gen.blankLine();
            for(var remoting: registry.getRemotings().values()){
                for(var group: remoting.getGroups().values()){
                    for(var subscription: group.getSubscriptions().values()){
                        gen.blankLine();
                        gen.wrapWithBlock("public static void %s_%s_send_event(%s event)".formatted(prepareName(group.getId()), prepareName(subscription.getId()), JavaCodeGeneratorUtils.getSimpleName(subscription.getEventClassName())), ()->{
                            gen.printLine("RemotingChannels.get().sendSubscriptionEvent(\"%s\",\"%s\",\"%s\", event);".formatted(
                                    remoting.getId(), group.getId(), subscription.getId()
                            ));
                        });
                    }
                }
            }
        });
        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), clientClassName + ".java", destDir);
        generatedFiles.add(file);
    }

    private String prepareName(String id) {
        return id.replace("-","_");
    }

}
