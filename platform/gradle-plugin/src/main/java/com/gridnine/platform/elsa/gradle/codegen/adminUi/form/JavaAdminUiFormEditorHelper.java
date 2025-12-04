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

package com.gridnine.platform.elsa.gradle.codegen.adminUi.form;

import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.BaseAdminUiFormComponentDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormComponentType;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormCustomElementDescription;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaAdminUiFormEditorHelper {

    public static void generateEditor(FormContainerDescription fd, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(fd.getClassName());
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.platform.elsa.admin.web.form.FormContainer");
        var simpleClassName = JavaCodeGeneratorUtils.getSimpleName(fd.getClassName());
        gen.wrapWithBlock("public class %s extends FormContainer".formatted(simpleClassName), () -> {
            for (var pd : fd.getComponents().values()) {
                gen.blankLine();
                String className = getType(pd);
                gen.addImport(className);
                var scn = JavaCodeGeneratorUtils.getSimpleName(className);
                gen.printLine("private final %s %s;".formatted(scn, pd.getId()));
            }
            gen.blankLine();
            gen.addImport("com.gridnine.webpeer.core.ui.OperationUiContext");
            gen.wrapWithBlock("public %s(String tag, OperationUiContext context, String width)".formatted(simpleClassName), () -> {
                gen.blankLine();
                gen.printLine("super(tag, context);");
                gen.printLine("this.setWidth(width, context);");
                final var idx = new AtomicInteger(-1);
                for (var pd : fd.getComponents().values()) {
                    gen.blankLine();
                    gen.wrapWithBlock("", ()->{
                        var type = getType(pd);
                        var config = "%sConfiguration".formatted(type);
                        gen.addImport(config);
                        if(pd instanceof FormCustomElementDescription fcd){
                            gen.printLine("this.%s = new %s(\"%s\", context);".formatted(pd.getId(), JavaCodeGeneratorUtils.getSimpleName(type), pd.getId()));
                        } else {
                            gen.printLine("var config = new %s();".formatted(JavaCodeGeneratorUtils.getSimpleName(config)));
                            gen.addImport("com.gridnine.platform.elsa.common.core.utils.LocaleUtils");
                            gen.printLine("config.setDeferred(true);");
                            gen.printLine("config.setTitle(LocaleUtils.getLocalizedName(new String[]{%s}, new String[]{%s}, \"%s\"));"
                                    .formatted(BuildTextUtils.joinToString(pd.getTitle().keySet().stream().map("\"%s\""::formatted).toList(),
                                                    ","),
                                            BuildTextUtils.joinToString(pd.getTitle().values().stream().map("\"%s\""::formatted).toList(),
                                                    ", "), pd.getId()));
                            gen.printLine("this.%s = new %s(\"%s\", config, context);".formatted(pd.getId(), JavaCodeGeneratorUtils.getSimpleName(type), pd.getId()));
                        }
                        gen.printLine("addChild(context, this.%s, %s);".formatted(pd.getId(), idx.incrementAndGet()));
                    });
                }
            });
            for (var pd : fd.getComponents().values()) {
                gen.blankLine();
                String className = getType(pd);
                gen.addImport(className);
                gen.wrapWithBlock("public %s get%s()".formatted(JavaCodeGeneratorUtils.getSimpleName(className), BuildTextUtils.capitalize(pd.getId())), () -> gen.printLine("return %s;".formatted(pd.getId())));
            }

        });

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), fd.getClassName() + ".java", destDir);
        generatedFiles.add(file);
    }

    private static String getType(BaseAdminUiFormComponentDescription pd) {
        return switch (pd.getType()) {
            case TEXT_FIELD -> "com.gridnine.platform.elsa.admin.web.form.FormTextField";
            case BOOLEAN_FIELD -> "com.gridnine.platform.elsa.admin.web.form.FormBooleanField";
            case DATE_INTERVAL_FIELD -> "com.gridnine.platform.elsa.admin.web.form.FormDateIntervalField";
            case TEXT_AREA -> "com.gridnine.platform.elsa.admin.web.form.FormTextArea";
            case REMOTE_SELECT -> "com.gridnine.platform.elsa.admin.web.form.FormRemoteSelect";
            case SELECT -> "com.gridnine.platform.elsa.admin.web.form.FormSelect";
            case CUSTOM -> ((FormCustomElementDescription) pd).getClassName();
        };
    }

    public static void collectFields(FormContainerDescription cd, Map<String, String> fields) {
        cd.getComponents().forEach((pd, component) -> {
            fields.put(pd, getType(component));
        });
    }

    public static void generateNestedComponent(FormContainerDescription cd, String containerName, JavaCodeGenerator gen) {
        gen.addImport("com.gridnine.platform.elsa.admin.web.form.FormContainer");
        gen.printLine("var %s = new FormContainer(\"form\", context);".formatted(containerName));
        var idx = new AtomicInteger(0);
        for (var pd : cd.getComponents().values()) {
            gen.blankLine();
            gen.wrapWithBlock("", ()->{
                var type = getType(pd);
                var config = "%sConfiguration".formatted(type);
                gen.addImport(config);
                if(pd instanceof FormCustomElementDescription fcd){
                    gen.printLine("%s = new %s(\"%s\", context);".formatted(pd.getId(), JavaCodeGeneratorUtils.getSimpleName(type), pd.getId()));
                } else {
                    gen.printLine("var config = new %s();".formatted(JavaCodeGeneratorUtils.getSimpleName(config)));
                    gen.addImport("com.gridnine.platform.elsa.common.core.utils.LocaleUtils");
                    gen.printLine("config.setDeferred(true);");
                    gen.printLine("config.setTitle(LocaleUtils.getLocalizedName(new String[]{%s}, new String[]{%s}, \"%s\"));"
                            .formatted(BuildTextUtils.joinToString(pd.getTitle().keySet().stream().map("\"%s\""::formatted).toList(),
                                            ","),
                                    BuildTextUtils.joinToString(pd.getTitle().values().stream().map("\"%s\""::formatted).toList(),
                                            ", "), pd.getId()));
                    gen.printLine("%s = new %s(\"%s\", config, context);".formatted(pd.getId(), JavaCodeGeneratorUtils.getSimpleName(type), pd.getId()));
                }
                gen.printLine("%s.addChild(context, %s, %s);".formatted(containerName, pd.getId(), idx.getAndIncrement()));
            });
        }
    }
}
