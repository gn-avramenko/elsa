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
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.webApp.ContainerWebElementDescription;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebAppMetaRegistry;
import com.gridnine.platform.elsa.gradle.meta.webApp.WebElementWithChildren;
import com.gridnine.platform.elsa.gradle.parser.webApp.WebAppMetadataHelper;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class JavaWebAppElementsHelper {
    public static void generate(WebAppMetaRegistry registry, File destDir, File sourceDir, Set<File> generatedFiles) throws Exception {
        registry.getElements().values().forEach(element -> {
            BuildExceptionUtils.wrapException(() -> {
                var elm = WebAppMetadataHelper.toCustomEntity(element);
                var managedConfiguration = switch (element.getType()) {
                    case CONTAINER -> ((ContainerWebElementDescription) element).isManagedConfiguration();
                    default -> true;
                };
                {
                    var fields = new ArrayList<Property>();
                    if (element instanceof WebElementWithChildren ctr) {
                        for (var entry : ctr.getChildren().entrySet()) {
                            var child = entry.getValue();
                            fields.add(new Property(entry.getKey(), StandardValueType.ENTITY, child.getClassName(), false,  WebAppMetadataHelper.isManagedConfiguration(child)? PropertyType.MANAGED_CHILD: PropertyType.CHILD));
                        }
                    }
                    for (var command : elm.getCommandsFromClient()) {
                        if (command.getCollections().isEmpty() && command.getProperties().isEmpty()) {
                            fields.add(new Property("%sListener".formatted(command.getId()), StandardValueType.ENTITY, "com.gridnine.platform.elsa.common.core.model.common.RunnableWithExceptionAndArgument<com.gridnine.webpeer.core.ui.OperationUiContext>", false, PropertyType.COMMAND_FROM_CLIENT));
                        }
                    }
                    for (var property : elm.getServerManagedState().getProperties().values()) {
                        fields.add(new Property(property.getId(), property.getType(), property.getClassName(), false, PropertyType.STANDARD));
                    }

                    for (var coll : elm.getServerManagedState().getCollections().values()) {
                        fields.add(new Property(coll.getId(), coll.getElementType(), coll.getId(), true, PropertyType.STANDARD));
                    }
                    if(elm.getInputs().size() == 1 ){
                        var value = elm.getInputs().entrySet().iterator().next().getValue();
                        if (value.getSettings() != null) {
                            if(value.getSettings().getCollections().size() + value.getSettings().getProperties().size() == 1) {
                                for (var property : value.getSettings().getProperties().values()) {
                                    fields.add(new Property(property.getId(), property.getType(), property.getClassName(), false, PropertyType.STANDARD));
                                }
                                for (var coll : value.getSettings().getCollections().values()) {
                                    fields.add(new Property(coll.getId(), coll.getElementType(), coll.getElementClassName(), true, PropertyType.STANDARD));
                                }
                            }
                        }
                        if (value.getValue() != null) {
                            if(value.getValue().getCollections().size() + value.getValue().getProperties().size() == 1) {
                                for (var property : value.getValue().getProperties().values()) {
                                    fields.add(new Property(property.getId(), property.getType(), property.getClassName(), false, PropertyType.INPUT));
                                    fields.add(new Property("%sChangeListener".formatted(property.getId()), StandardValueType.ENTITY, "com.gridnine.platform.elsa.webApp.WebAppValueChangeListener<%s>".formatted(WebAppMetadataHelper.getPropertyType(property.getType(), property.getClassName())), false, PropertyType.INPUT_CHANGE_LISTENER));
                                    fields.add(new Property("has%sChangeListener".formatted(BuildTextUtils.capitalize(property.getId())), StandardValueType.BOOLEAN, null, false, PropertyType.INPUT_HAS_CHANGE_LISTENER));
                                }
                                for (var coll : value.getValue().getCollections().values()) {
                                    fields.add(new Property(coll.getId(), coll.getElementType(), coll.getId(), true, PropertyType.INPUT));
                                    fields.add(new Property("%sChangeListener".formatted(coll.getId()), StandardValueType.ENTITY, "com.gridnine.platform.elsa.webApp.WebAppValueChangeListener<java.util.List<%s>>".formatted(WebAppMetadataHelper.getPropertyType(coll.getElementType(), coll.getElementClassName())), false, PropertyType.INPUT_CHANGE_LISTENER));
                                    fields.add(new Property("has%sChangeListener".formatted(BuildTextUtils.capitalize(coll.getId())), StandardValueType.BOOLEAN, null, false, PropertyType.INPUT_HAS_CHANGE_LISTENER));
                                }
                            }
                        }
                    }


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
                        for(var field: fields) {
                            switch (field.type){
                                case INPUT,INPUT_CHANGE_LISTENER,COMMAND_FROM_CLIENT ->{
                                    if (field.collection) {
                                        gen.addImport("java.util.List");
                                        gen.addImport("java.util.ArrayList");
                                        gen.printLine("private List<%s> %s = new ArrayList<>();".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), field.id));
                                    } else {
                                        gen.printLine("private %s %s;".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen), field.id));
                                    }
                                }

                                case MANAGED_CHILD,CHILD->{
                                    gen.printLine("private final %s %s;".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen), field.id));
                                }
                            }
                        }
                        var cns = managedConfiguration ? "public %sSkeleton(String tag, %sConfiguration config, OperationUiContext ctx)".formatted(simpleClassName, simpleClassName) :
                                "public %sSkeleton(String tag, OperationUiContext ctx)".formatted(simpleClassName);
                        gen.blankLine();
                        gen.wrapWithBlock(cns, () -> {
                            gen.printLine("super(\"%s\", tag, ctx);".formatted(className));
                            if (!managedConfiguration) {
                                gen.printLine("var config = createConfiguration(ctx);");
                            }
                            var childrenCount = 0;
                            for (var field : fields) {
                                switch (field.type) {
                                    case CHILD -> {
                                        gen.printLine("%s = new %s(\"%s\", ctx);".formatted(field.id, JavaCodeGeneratorUtils.getSimpleName(field.className), field.id));
                                        gen.printLine("addChild(ctx, %s, %s);".formatted(field.id, childrenCount));
                                        childrenCount++;
                                    }
                                    case MANAGED_CHILD -> {
                                        gen.printLine("%s = new %s(\"%s\", config.get%s(), ctx);".formatted(field.id, JavaCodeGeneratorUtils.getSimpleName(field.className), field.id, BuildTextUtils.capitalize(field.id)));
                                        gen.printLine("addChild(ctx, %s, %s);".formatted(field.id, childrenCount));
                                        childrenCount++;
                                    }
                                    case INPUT_HAS_CHANGE_LISTENER -> {
                                        //noops
                                    }
                                    default -> gen.printLine("set%s(config.%s%s(), ctx);".formatted(BuildTextUtils.capitalize(field.id), field.valueType == StandardValueType.BOOLEAN? "is":"get", BuildTextUtils.capitalize(field.id)));
                                }
                            }
                        });
                        for(var field: fields) {
                            if(field.type == PropertyType.INPUT_HAS_CHANGE_LISTENER){
                                continue;
                            }
                            if(field.collection){
                                switch (field.type) {
                                    case INPUT -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public List<%s> get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return (List<%s>) state.get(\"%s\");".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), field.id));
                                        });
                                        gen.blankLine();
                                        gen.wrapWithBlock("public void set%s(List<%s> value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(field.id), JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen)), () -> {
                                            gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                            gen.wrapWithBlock("if(WebAppUtils.equals(value, this.%s))".formatted(field.id), ()->{
                                                gen.printLine("return;");
                                            });
                                            gen.printLine("state.put(\"%s\", value);".formatted(field.id));
                                            gen.printLine("this.%s = value;".formatted(field.id));
                                            gen.wrapWithBlock("if(initialized)", () -> {
                                                gen.addImport("com.google.gson.JsonObject");
                                                gen.printLine("var data = new JsonObject();");
                                                gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(field.id));
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value, StandardValueType.%s));".formatted(field.valueType.name()));
                                                gen.printLine("sendCommand(context, \"pc\", data);");
                                            });
                                        });
                                    }
                                    default -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public List<%s> get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return (List<%s>) state.get(\"%s\");".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), field.id));
                                        });
                                        gen.blankLine();
                                        gen.wrapWithBlock("public void set%s(List<%s> value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(field.id), JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen)), () -> {
                                            gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                            gen.wrapWithBlock("if(WebAppUtils.equals(value, state.get(\"%s\")))".formatted(field.id), ()->{
                                                gen.printLine("return;");
                                            });
                                            gen.printLine("state.put(\"%s\", value);".formatted(field.id));
                                            gen.wrapWithBlock("if(initialized)", () -> {
                                                gen.addImport("com.google.gson.JsonObject");
                                                gen.printLine("var data = new JsonObject();");
                                                gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(field.id));
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value, StandardValueType.%s));".formatted(field.valueType.name()));
                                                gen.printLine("sendCommand(context, \"pc\", data);");
                                            });
                                        });
                                    }
                                }

                            } else {

                                gen.blankLine();
                                switch (field.type) {
                                    case COMMAND_FROM_CLIENT -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public %s %s%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen), field.valueType == StandardValueType.BOOLEAN? "is":"get", BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return this.%s;".formatted( field.id));
                                        });
                                        gen.blankLine();
                                        gen.wrapWithBlock("public void set%s(%s value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(field.id), JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen)), () -> {
                                            gen.printLine("this.%s = value;".formatted(field.id));
                                        });
                                    }
                                    case INPUT_CHANGE_LISTENER -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public %s get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen), BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return %s;".formatted(field.id));
                                        });
                                        gen.blankLine();
                                        gen.wrapWithBlock("public void set%s(%s value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(field.id), JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, false, gen)), () -> {
                                            gen.printLine("this.%s = value;".formatted(field.id));
                                            gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                            var propertyName = "has%s".formatted(BuildTextUtils.capitalize(field.id));
                                            gen.wrapWithBlock("if(WebAppUtils.equals(value != null, state.get(\"%s\")))".formatted(propertyName), ()->{
                                                gen.printLine("return;");
                                            });
                                            gen.printLine("state.put(\"%s\", value != null);".formatted(propertyName));
                                            gen.wrapWithBlock("if(initialized)", () -> {
                                                gen.addImport("com.google.gson.JsonObject");
                                                gen.printLine("var data = new JsonObject();");
                                                gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(propertyName));
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value != null, StandardValueType.BOOLEAN));");
                                                gen.printLine("sendCommand(context, \"pc\", data);");
                                            });
                                        });
                                    }
                                    case CHILD,MANAGED_CHILD -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public %s get%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen),  BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return this.%s;".formatted(field.id));
                                        });
                                    }
                                    default -> {
                                        gen.blankLine();
                                        gen.wrapWithBlock("public %s %s%s()".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen), field.valueType == StandardValueType.BOOLEAN? "is":"get", BuildTextUtils.capitalize(field.id)), () -> {
                                            gen.printLine("return (%s) state.get(\"%s\");".formatted(JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen), field.id));
                                        });
                                        gen.blankLine();
                                        gen.wrapWithBlock("public void set%s(%s value, OperationUiContext context)".formatted(BuildTextUtils.capitalize(field.id), JavaCodeGeneratorUtils.getPropertyType(field.valueType, field.className, true, gen)), () -> {
                                            gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                            gen.wrapWithBlock("if(WebAppUtils.equals(value, state.get(\"%s\")))".formatted(field.id), ()->{
                                                gen.printLine("return;");
                                            });
                                            gen.printLine("state.put(\"%s\", value);".formatted(field.id));
                                            gen.wrapWithBlock("if(initialized)", () -> {
                                                gen.addImport("com.google.gson.JsonObject");
                                                gen.printLine("var data = new JsonObject();");
                                                gen.printLine("data.addProperty(\"pn\", \"%s\");".formatted(field.id));
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("data.add(\"pv\", WebAppUtils.toJsonValue(value, StandardValueType.%s));".formatted(field.valueType.name()));
                                                gen.printLine("sendCommand(context, \"pc\", data);");
                                            });
                                        });
                                    }
                                }


                            }
                        }
                        var inputs = fields.stream().filter(it -> it.type == PropertyType.INPUT).toList();
                        if (!elm.getCommandsFromClient().isEmpty() || !inputs.isEmpty()) {
                            gen.printLine("@Override");
                            gen.addImport("com.google.gson.JsonElement");
                            gen.wrapWithBlock("public void processCommand(OperationUiContext ctx, String commandId, JsonElement data)  throws Exception", () -> {
                                if(!inputs.isEmpty()) {
                                    gen.wrapWithBlock("if(commandId.equals(\"pc\"))", ()->{
                                        gen.printLine("var obj = data.getAsJsonObject();");
                                        gen.addImport("com.gridnine.webpeer.core.utils.WebPeerUtils");
                                        gen.printLine("var pn = WebPeerUtils.getString(obj, \"pn\");");
                                        gen.printLine("var pv = obj.get(\"pv\");");
                                        for(var input: inputs){
                                            gen.wrapWithBlock("if(pn.equals(\"%s\"))".formatted(input.id), ()->{
                                               gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                               gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                               gen.printLine("var oldValue = this.%s;".formatted(input.id));
                                               if(input.collection){
                                                   gen.printLine("var newValue = WebAppUtils.fromJsonArrayValue(pv, StandardValueType.%s, \"%s\", %s.class);".formatted(input.valueType, input.className, getPropertyType(input.valueType, input.className, gen)));
                                               } else {
                                                   gen.printLine("var newValue = WebAppUtils.fromJsonValue(pv, StandardValueType.%s, \"%s\", %s.class);".formatted(input.valueType, input.className, getPropertyType(input.valueType, input.className, gen)));
                                               }
                                                gen.printLine("this.%s = newValue;".formatted(input.id));
                                               gen.wrapWithBlock("if(this.%sChangeListener != null)".formatted(input.id), ()->{
                                                   gen.printLine("this.%sChangeListener.onValueChange(oldValue, newValue, ctx);".formatted(input.id));
                                               });
                                               gen.printLine("return;");
                                            });
                                        }
                                    });

                                }
                                for (var action : elm.getCommandsFromClient()) {
                                    if (action.getCollections().isEmpty() && action.getProperties().isEmpty()) {
                                        gen.wrapWithBlock("if(commandId.equals(\"%s\"))".formatted(action.getId()), () -> {
                                            gen.printLine("this.%sListener.run(ctx);".formatted(action.getId()));
                                            gen.printLine("return;");
                                        });
                                        continue;
                                    }
                                    throw new Exception("not implemented");
                                }
                                gen.printLine("super.processCommand(ctx, commandId, data);");
                            });
                        }
                        gen.blankLine();
                        gen.printLine("@Override");
                        gen.addImport("com.google.gson.JsonObject");
                        gen.wrapWithBlock("public JsonObject buildState(OperationUiContext context)", () -> {
                            gen.printLine("var result =  super.buildState(context);");
                            for(var field: fields){
                                switch (field.type){
                                    case STANDARD,INPUT,INPUT_HAS_CHANGE_LISTENER -> {
                                        if(field.collection){
                                            gen.wrapWithBlock("if(state.get(\"%s\") != null && !((List<?>)state.get(\"%s\")).isEmpty())".formatted(field.id, field.id), () -> {
                                                gen.addImport("com.google.gson.JsonArray");
                                                gen.printLine("var coll = new JsonArray();");
                                                gen.wrapWithBlock("for(var elm: (List<?>) state.get(\"%s\"))".formatted(field.id), () -> {
                                                    gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                    gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                    gen.printLine("coll.add(WebAppUtils.toJsonValue(elm, StandardValueType.%s));".formatted(field.valueType.name()));
                                                });
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("result.add(\"%s\", coll);".formatted(field.id));
                                            });
                                        } else {
                                            gen.wrapWithBlock("if(state.get(\"%s\") != null)".formatted(field.id), () -> {
                                                gen.addImport("com.gridnine.platform.elsa.webApp.WebAppUtils");
                                                gen.addImport("com.gridnine.platform.elsa.common.meta.common.StandardValueType");
                                                gen.printLine("result.add(\"%s\", WebAppUtils.toJsonValue(state.get(\"%s\"), StandardValueType.%s));".formatted(field.id, field.id, field.valueType.name()));
                                            });
                                        }
                                    }
                                    default -> {

                                    }
                                }

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

    record Property(String id, StandardValueType valueType, String className, boolean collection, PropertyType type) {

    }

    enum PropertyType {
        STANDARD,
        INPUT,
        INPUT_CHANGE_LISTENER,
        INPUT_HAS_CHANGE_LISTENER,
        CHILD,
        MANAGED_CHILD,
        COMMAND_FROM_CLIENT
    }

    public static String getPropertyType(StandardValueType type, String className, JavaCodeGenerator gen) {
        return switch (type) {
            case STRING, CLASS -> "String";
            case LOCAL_DATE -> {
                gen.addImport(LocalDate.class.getName());
                yield LocalDate.class.getSimpleName();
            }
            case LOCAL_DATE_TIME -> {
                gen.addImport(LocalDateTime.class.getName());
                yield LocalDateTime.class.getName();
            }
            case INSTANT -> {
                gen.addImport(Instant.class.getName());
                yield Instant.class.getName();
            }
            case BOOLEAN -> "Boolean";
            case ENUM, ENTITY -> {
                gen.addImport(className);
                yield JavaCodeGeneratorUtils.getSimpleName(className);
            }
            case LONG -> "Long";
            case INT -> "Integer";
            case BIG_DECIMAL -> {
                gen.addImport(BigDecimal.class.getName());
                yield BigDecimal.class.getName();
            }
            default -> throw new IllegalStateException("Unknown property type: " + type);
        };
    }
}
