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

package com.gridnine.platform.elsa.gradle.codegen.webApp.helpers;

import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.WebCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.remoting.RemotingMetaRegistry;
import com.gridnine.platform.elsa.gradle.meta.webApp.ContainerWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebAppMetaRegistry;
import com.gridnine.platform.elsa.gradle.parser.webApp.WebAppMetadataHelper;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class WebWebAppElementsHelper {
    public static void generate(WebAppMetaRegistry registry, File destDir, File sourceDir, Set<File> generatedFiles) throws Exception {
        registry.getElements().values().forEach(element -> {
            BuildExceptionUtils.wrapException(() -> {
                var elm = WebAppMetadataHelper.extendWithStandardProperties(element);
                {
                    var className = elm.getClassName();
                    var simpleClassName = JavaCodeGeneratorUtils.getSimpleName(className);
                    var gen = new TypeScriptCodeGenerator();
                    var stateStr = new StringBuilder();
                    var stateKeys = new ArrayList<String>();
                    stateKeys.addAll(elm.getServerManagedState().getProperties().keySet());
                    stateKeys.addAll(elm.getServerManagedState().getCollections().keySet());
                    stateStr.append(BuildTextUtils.joinToString(stateKeys.stream().map("\"%s\""::formatted).toList(), ", "));
                    var sa = new StringBuilder();
                    sa.append(BuildTextUtils.joinToString(elm.getCommandsFromServer().stream().map(it -> "\"%s\"".formatted(it.getId())).toList(), ", "));
                    var ca = new StringBuilder();
                    ca.append(BuildTextUtils.joinToString(elm.getCommandsFromServer().stream().map(it -> "\"%s\"".formatted(it.getId())).toList(), ", "));
                    gen.addImport("{BaseReactUiElement} from '@/common/component'");
                    gen.wrapWithBlock("export abstract class %sSkeleton extends BaseReactUiElement".formatted(simpleClassName), () -> {
                        gen.wrapWithBlock("constructor(model: any)", () -> {
                            gen.printLine("""
                                    super({
                                       state: [%s],
                                       actionsFromServer: [%s],
                                       actionsFromClient: [%s],
                                    }, model);
                                    """.formatted(stateStr, sa, ca));
                            for (var prop : elm.getServerManagedState().getProperties().values()) {
                                gen.printLine("this.state.set('%s', model.%s);".formatted(prop.getId(), prop.getId()));
                            }
                            for (var coll : elm.getServerManagedState().getCollections().values()) {
                                gen.printLine("this.state.set('%s', model.%s??[]);".formatted(coll.getId(), coll.getId()));
                            }
                        });
                        gen.blankLine();
                        for (var prop : elm.getServerManagedState().getProperties().values()) {
                            gen.wrapWithBlock("get%s()".formatted(BuildTextUtils.capitalize(prop.getId())), () -> {
                                gen.printLine("return this.state.get('%s') as %s%s".formatted(prop.getId(),
                                        getType(prop.getType(), registry, prop.getClassName(), gen),
                                        prop.isNonNullable() ? "" : " | undefined"));
                            });
                        }
                        for (var coll : elm.getServerManagedState().getCollections().values()) {
                            gen.wrapWithBlock("get%s()".formatted(BuildTextUtils.capitalize(coll.getId())), () -> {
                                gen.printLine("return this.state.get('%s') as %s[] | undefined".formatted(coll.getId(),
                                        getType(coll.getElementType(), registry, coll.getElementClassName(), gen)));
                            });
                        }
                    });

                    var file = WebCodeGeneratorUtils.saveIfDiffers(gen.toString(), WebCodeGeneratorUtils.getFile(className + "Skeleton.tsx", destDir));
                    generatedFiles.add(file);
                }
                switch (element.getType()) {
                    case CONTAINER -> WebContainerHelper.generateContainer((ContainerWebElementDescription) element, sourceDir);
                }

            });

        });
    }

    private static File getSourceFile(File sourceDir, String className) {
        var parts = className.split("\\.");
        var currentFile = sourceDir;
        var length = parts.length;
        for (int n = 0; n < length - 1; n++) {
            currentFile = new File(currentFile, parts[n] + "/");
        }
        return new File(currentFile, parts[parts.length - 1] + ".java");
    }

    public static String getImportName(String className) {
        if("com.gridnine.platform.elsa.webApp.common.FlexDirection".equals(className)){
            return "@g/common/FlexDirection";
        }
        var parts = className.split("\\.");
        StringBuilder s = new StringBuilder();
        var length = parts.length;
        for (int n = length - 2; n < length; n++) {
            if(!s.isEmpty()){
                s.append("/");
            }
            s.append(parts[n]);
        }
        return "@g/"+ s;
    }

    private static String getType(StandardValueType vt, WebAppMetaRegistry metaRegistry, String className, TypeScriptCodeGenerator gen) throws Exception {
        return switch (vt) {
            case LONG, INT, BIG_DECIMAL -> "number";
            case UUID, STRING, CLASS, LOCAL_DATE, INSTANT, LOCAL_DATE_TIME -> "string";
            case ENUM,ENTITY -> {
                var simpleName = JavaCodeGeneratorUtils.getSimpleName(className);
                var importName = getImportName(className);
                gen.addImport("{%s} from '%s'".formatted(simpleName, importName));
                yield  simpleName;
            }
            case BOOLEAN -> "boolean";
            default -> throw new Exception("Unsupported type: " + vt);

        };
    }
}


