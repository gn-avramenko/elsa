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

import com.gridnine.platform.elsa.gradle.codegen.common.GenEntityDescription;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.meta.webApp.ContainerWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebAppMetaRegistry;
import com.gridnine.platform.elsa.gradle.parser.webApp.WebAppMetadataHelper;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;

public class JavaWebAppElementsHelper {
    public static void generate(WebAppMetaRegistry registry, File destDir, File sourceDir, Set<File> generatedFiles) throws Exception {
        registry.getElements().values().forEach(element -> {
            BuildExceptionUtils.wrapException(() -> {
                var elm = WebAppMetadataHelper.extendWithStandardProperties(element);
                var managedConfiguration = (element instanceof ContainerWebElementDescription) && ((ContainerWebElementDescription) element).isManagedConfiguration();
                {
                    var className = elm.getClassName();
                    var simpleClassName = JavaCodeGeneratorUtils.getSimpleName(className);
                    var gen = new JavaCodeGenerator();
                    var packageName = JavaCodeGeneratorUtils.getPackage(className);
                    gen.setPackageName(packageName);
                    gen.addImport("com.gridnine.webpeer.core.ui.OperationUiContext");
                    gen.wrapWithBlock("public abstract class %sSkeleton extends BaseUiElement".formatted(simpleClassName), () -> {
                        gen.printLine("private boolean initialized;");
                        gen.blankLine();
                        gen.addImport("java.util.HashMap");
                        gen.addImport("java.util.Map");
                        gen.printLine("private final Map<String, Object> state = new HashMap<>();");
                        gen.addImport("com.gridnine.webpeer.core.ui.BaseUiElement");
                        var cns = managedConfiguration ? "public %sSkeleton(String tag, %sConfiguration config, OperationUiContext ctx)".formatted(simpleClassName, simpleClassName) :
                                "public %sSkeleton(String tag, OperationUiContext ctx)".formatted(simpleClassName);
                        gen.blankLine();
                        gen.wrapWithBlock(cns, () -> {
                            gen.printLine("super(\"%s\", tag, ctx);".formatted(className));
                            if (!managedConfiguration) {
                                gen.printLine("var config = createConfiguration(ctx);");
                            }
                            for(var property: elm.getServerManagedState().getProperties().values()){
                                gen.printLine("set%s(config.get%s(), ctx);".formatted(BuildTextUtils.capitalize(property.getId()), BuildTextUtils.capitalize(property.getId())));
                            }
                            for(var coll: elm.getServerManagedState().getCollections().values()){
                                gen.printLine("set%s(config.get%s(), ctx);".formatted(BuildTextUtils.capitalize(coll.getId()), BuildTextUtils.capitalize(coll.getId())));
                            }
                        });
                        for(var property: elm.getServerManagedState().getProperties().values()){
                            gen.blankLine();
                            gen.wrapWithBlock("public %s get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(property.getType(), property.getClassName(), property.isNonNullable(), gen), BuildTextUtils.capitalize(property.getId())), ()->{
                                gen.printLine("return (%s) state.get(\"%s\");".formatted(JavaCodeGeneratorUtils.getPropertyType(property.getType(), property.getClassName(), false, gen), property.getId()));
                            });
                            gen.blankLine();
                            gen.wrapWithBlock("public void set%s(%s value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(property.getId()), JavaCodeGeneratorUtils.getPropertyType(property.getType(), property.getClassName(), property.isNonNullable(), gen)), ()->{
                                gen.printLine("state.put(\"%s\", value);".formatted(property.getId()));
                                gen.wrapWithBlock("if(initialized)", ()->{
                                    gen.addImport("com.google.gson.JsonObject");
                                    gen.printLine("var data = new JsonObject();");
                                    gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(property.getId()));
                                    gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                    gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                    gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value, StandardValueType.%s));".formatted(property.getType().name()));
                                    gen.printLine("sendCommand(context, \"pc\", data);");
                                });
                            });
                        }
                        for(var coll: elm.getServerManagedState().getCollections().values()){
                            gen.blankLine();
                            gen.addImport("java.util.List");
                            gen.wrapWithBlock("public List<%s> get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(coll.getElementType(), coll.getElementClassName(), false, gen), BuildTextUtils.capitalize(coll.getId())), ()->{
                                gen.printLine("return (List<%s>) state.get(\"%s\");".formatted(JavaCodeGeneratorUtils.getPropertyType(coll.getElementType(), coll.getElementClassName(), false, gen), coll.getId()));
                            });
                            gen.blankLine();
                            gen.wrapWithBlock("public void set%s(List<%s> value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(coll.getId()), JavaCodeGeneratorUtils.getPropertyType(coll.getElementType(), coll.getElementClassName(), false, gen)), ()->{
                                gen.printLine("state.put(\"%s\", value);".formatted(coll.getId()));
                                gen.wrapWithBlock("if(initialized)", ()->{
                                    gen.addImport("com.google.gson.JsonObject");
                                    gen.printLine("var data = new JsonObject();");
                                    gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(coll.getId()));
                                    gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                    gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                    gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value, StandardValueType.%s));".formatted(coll.getElementType().name()));
                                    gen.printLine("sendCommand(context, \"pc\", data);");
                                });
                            });
                        }
                        gen.blankLine();
                        gen.printLine("@Override");
                        gen.addImport("com.google.gson.JsonObject");
                        gen.wrapWithBlock("public JsonObject buildState(OperationUiContext context)", () -> {
                            gen.printLine("var result =  super.buildState(context);");
                            for (var prop : elm.getServerManagedState().getProperties().values()) {
                                gen.wrapWithBlock("if(state.get(\"%s\") != null)".formatted(prop.getId()), () -> {
                                    gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                    gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                    gen.printLine("result.add(\"%s\", WebAppUtils.toJsonValue(state.get(\"%s\"), StandardValueType.%s));".formatted(prop.getId(), prop.getId(), prop.getType().name()));
                                });
                            }
                            for (var coll : elm.getServerManagedState().getCollections().values()) {
                                gen.wrapWithBlock("if(state.get(\"%s\") != null && !state.get(\"%s\").isEmpty())".formatted(coll.getId(), coll.getId()), () -> {
                                    gen.printLine("var col = new JsonArray();");
                                    gen.wrapWithBlock("for(var elm: state.get(\"%s\"))".formatted(coll.getId()), () -> {
                                        gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                        gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                        gen.printLine("coll.add(WebAppUtils.toJsonValue(elm, StandardValueType.%s));".formatted(coll.getElementType().name()));
                                    });
                                    gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                    gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                    gen.printLine("result.add(\"%s\", coll);");
                                });
                            }
                            gen.printLine("return result;");
                        });
                        if (!managedConfiguration) {
                            gen.blankLine();
                            gen.printLine("protected abstract %sConfiguration createConfiguration(OperationUiContext ctx);".formatted(simpleClassName));
                        }
                    });
                    var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), className + "Skeleton.java", destDir);
                    generatedFiles.add(file);
                }
                {
                    var sourceFile = getSourceFile(sourceDir, element.getClassName());
                    if (!sourceFile.exists()) {
                        var className = elm.getClassName();
                        var simpleClassName = JavaCodeGeneratorUtils.getSimpleName(className);
                        var gen = new JavaCodeGenerator();
                        var packageName = JavaCodeGeneratorUtils.getPackage(className);
                        gen.setPackageName(packageName);
                        gen.addImport("com.gridnine.webpeer.core.ui.OperationUiContext");
                        gen.wrapWithBlock("public class %s extends %sSkeleton".formatted(simpleClassName, simpleClassName), () -> {
                            var cns = managedConfiguration ? "public %s(String tag, %sConfiguration config, OperationUiContext ctx)".formatted(simpleClassName, simpleClassName) :
                                    "public %s(String tag, OperationUiContext ctx)".formatted(simpleClassName);
                            gen.blankLine();
                            gen.wrapWithBlock(cns, () -> {
                                gen.printLine(managedConfiguration ? "super(tag, config, ctx);" : "super(tag, ctx);");
                            });
                        });
                        if (!sourceFile.getParentFile().exists()) {
                            sourceFile.getParentFile().mkdirs();
                        }
                        Files.writeString(sourceFile.toPath(), gen.toString());
                    }
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
}
