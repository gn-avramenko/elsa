/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.gradle.codegen.remoting;

import com.gridnine.platform.elsa.common.meta.common.EntityDescription;
import com.gridnine.platform.elsa.common.meta.common.StandardValueType;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.platform.elsa.gradle.codegen.common.CodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.WebCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.parser.remoting.RemotingMetaRegistryParser;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WebRemotingCodeGeneratorBack implements CodeGenerator<WebRemotingCodeGenRecord> {
    @Override
    public void generate(WebRemotingCodeGenRecord record, File destDir, Set<File> generatedFiles, Map<Object, Object> context) {
            var parser = new RemotingMetaRegistryParser();
            var metaRegistry = new RemotingMetaRegistry();
            parser.updateMetaRegistry(metaRegistry, record.getSources());
            BuildExceptionUtils.wrapException(() -> {
                var gen = new TypeScriptCodeGenerator();
                gen.printLine("import {\n  serverCall, PreloaderHandler, subscribe, unsubscribe,\n} from 'elsa-web-core';");
                gen.blankLine();
                var additionalEntities = new HashSet<String>();
                metaRegistry.getEntities().values().forEach(ett -> collectAdditionalEntities(additionalEntities, ett, metaRegistry));
                @SuppressWarnings("unchecked") var tsa = (Map<String, File>) context.get("ts-associations");
                if (tsa == null) {
                    tsa = new LinkedHashMap<>();
                    context.put("ts-associations", tsa);
                }
                for (var en : metaRegistry.getEnums().values()) {
                    WebCodeGeneratorUtils.generateWebEnumCode(en, gen);
                }
                for (var ett : metaRegistry.getEntities().values()) {
                    WebCodeGeneratorUtils.generateWebEntityCode(ett, gen);
                }

                for (var remoting : metaRegistry.getRemotings().values()) {
                    for (var group : remoting.getGroups().values()) {
                        for (var serverCall : group.getServices().values()) {
                            gen.printLine("// eslint-disable-next-line max-len");
                            gen.wrapWithBlock("export const %s = async (request: %s, preloaderHandler: PreloaderHandler| boolean = true, operationId: string|null = null) => "
                                            .formatted(JavaCodeGeneratorUtils.toCamelCased("%s_%s".formatted(group.getId(), serverCall.getId())),
                                                    JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()))
                                    , () -> {
                                        gen.printLine("// noinspection UnnecessaryLocalVariableJS");
                                        gen.printLine("const result = await serverCall<%s, %s>('%s', '%s', '%s', request, preloaderHandler, operationId);"
                                                .formatted(JavaCodeGeneratorUtils.getSimpleName(serverCall.getRequestClassName()),
                                                        JavaCodeGeneratorUtils.getSimpleName(serverCall.getResponseClassName()), remoting.getId(), group.getId(), serverCall.getId()));
                                        gen.printLine("return result;");
                                    });
                            gen.print(";\n");
                        }
                        for (var subscription : group.getSubscriptions().values()) {
                            gen.printLine("// eslint-disable-next-line max-len,no-unused-vars");
                            gen.wrapWithBlock("export const %s = async (parameters: %s, handler: (ev:%s) => boolean, preloaderHandler: PreloaderHandler| boolean = true, operationId: string|null = null) => "
                                    .formatted(JavaCodeGeneratorUtils.toCamelCased("%s_%s".formatted(group.getId(), subscription.getId())),
                                            JavaCodeGeneratorUtils.getSimpleName(subscription.getParameterClassName()),
                                            JavaCodeGeneratorUtils.getSimpleName(subscription.getEventClassName())), () -> {
                                gen.printLine("const subId = await subscribe<%s, %s>('%s', '%s', '%s', parameters, handler, preloaderHandler, operationId);".formatted(
                                        JavaCodeGeneratorUtils.getSimpleName(subscription.getParameterClassName()), JavaCodeGeneratorUtils.getSimpleName(subscription.getEventClassName()),
                                        remoting.getId(), group.getId(), subscription.getId()
                                ));
                                gen.printLine("return subId;");
                            });
                            gen.print(";\n");
                            gen.printLine("// eslint-disable-next-line max-len,no-unused-vars");
                            gen.wrapWithBlock("export const %s = async (subId: string, preloaderHandler: PreloaderHandler| boolean = true, operationId: string|null = null) => "
                                    .formatted(JavaCodeGeneratorUtils.toCamelCased("unsubscribe_%s_%s".formatted(group.getId(), subscription.getId()))), () -> gen.printLine("await unsubscribe('%s', subId, preloaderHandler, operationId);".formatted(
                                    remoting.getId()))
                            );
                            gen.print(";\n");
                        }
                    }
                }
                var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "facade.ts", destDir);
                generatedFiles.add(file);
        });
    }

    private void collectAdditionalEntities(HashSet<String> additionalEntities, EntityDescription ett, RemotingMetaRegistry metaRegistry) {
        ett.getProperties().values().forEach(prop -> {
            if (prop.getType() == StandardValueType.ENTITY && !metaRegistry.getEntities().containsKey(prop.getClassName())) {
                additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(prop.getClassName()));
            }
        });
        ett.getCollections().values().forEach(prop -> {
            if (prop.getElementType() == StandardValueType.ENTITY && !metaRegistry.getEntities().containsKey(prop.getElementClassName())) {
                additionalEntities.add(JavaCodeGeneratorUtils.getSimpleName(prop.getElementClassName()));
            }
        });
    }
}
