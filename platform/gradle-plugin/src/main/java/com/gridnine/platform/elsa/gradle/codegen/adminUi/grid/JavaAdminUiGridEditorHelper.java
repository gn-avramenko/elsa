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

package com.gridnine.platform.elsa.gradle.codegen.adminUi.grid;

import com.gridnine.platform.elsa.gradle.codegen.adminUi.common.JavaAdminUiCodeGenUtils;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.BaseAdminUiFormComponentDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormCustomElementDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.grid.GridContainerDescription;
import com.gridnine.platform.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.platform.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaAdminUiGridEditorHelper {

    public static void generateEditor(GridContainerDescription fd, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(fd.getClassName());
        gen.setPackageName(packageName);
        gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridContainer");
        var simpleClassName = JavaCodeGeneratorUtils.getSimpleName(fd.getClassName());
        var fields = new LinkedHashMap<String, String>();
        JavaAdminUiCodeGenUtils.collectFields(fd, fields);
        gen.wrapWithBlock("public class %s extends GridContainer".formatted(simpleClassName), () -> {
            fields.forEach((fieldName, className) -> {
                gen.blankLine();
                gen.addImport(className);
                var scn = JavaCodeGeneratorUtils.getSimpleName(className);
                gen.printLine("private final %s %s;".formatted(scn, fieldName));
            });
            gen.blankLine();
            gen.addImport("com.gridnine.webpeer.core.ui.OperationUiContext");
            gen.wrapWithBlock("public %s(String tag, OperationUiContext context)".formatted(simpleClassName), () -> {
                gen.printLine("super(tag, context);");
                AtomicInteger rowNumber = new AtomicInteger(0);
                fd.getRows().forEach(row ->{
                    gen.wrapWithBlock("",()->{
                        gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridRowContainer");
                        gen.printLine("var row = new GridRowContainer(\"row\", context);");
                        var columnNumber  = new AtomicInteger(0);
                        row.getColumns().forEach(column -> {
                            gen.wrapWithBlock("", ()->{
                                gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridColumnContainer");
                                gen.printLine("var column = new GridColumnContainer(\"column\", context);");
                                gen.printLine("column.setSmallWidth(%s, context);".formatted(column.getSmallWidth()));
                                gen.printLine("column.setStandardWidth(%s, context);".formatted(column.getStandardWidth()));
                                gen.printLine("column.setLargeWidth(%s, context);".formatted(column.getLargeWidth()));
                                JavaAdminUiCodeGenUtils.generateNestedComponent(column.getContent(), "content", gen);
                                gen.printLine("column.addChild(context, content, 0);");
                                gen.printLine("row.addChild(context, column, %s);".formatted(columnNumber.getAndIncrement()));
                            });
                        });
                        gen.printLine("addChild(context, row, %s);".formatted(rowNumber.getAndIncrement()));
                    });
                });
            });
            fields.forEach((fieldName, className) -> {
                gen.blankLine();
                gen.addImport(className);
                gen.wrapWithBlock("public %s get%s()".formatted(JavaCodeGeneratorUtils.getSimpleName(className), BuildTextUtils.capitalize(fieldName)), ()->{
                    gen.printLine("return this.%s;".formatted(fieldName));
                });
            });
        });

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), fd.getClassName() + ".java", destDir);
        generatedFiles.add(file);
    }


    public static void generateNestedComponent(GridContainerDescription cd, String containerName, JavaCodeGenerator gen) {
        gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridContainer");
        gen.printLine("var %s = new GridContainer(\"grid\", context);".formatted(containerName));
        for (var row : cd.getRows()) {
            var rowNumber = new AtomicInteger(0);
            gen.wrapWithBlock("",()->{
                gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridRowContainer");
                gen.printLine("var row = new GridRowContainer(\"row\", context);");
                var columnNumber  = new AtomicInteger(0);
                row.getColumns().forEach(column -> {
                    gen.wrapWithBlock("", ()->{
                        gen.addImport("com.gridnine.platform.elsa.admin.web.grid.GridColumnContainer");
                        gen.printLine("var column = new GridColumnContainer(\"column\", context);");
                        gen.printLine("column.setSmallWidth(%s, context);".formatted(column.getSmallWidth()));
                        gen.printLine("column.setStandardWidth(%s, context);".formatted(column.getStandardWidth()));
                        gen.printLine("column.setLargeWidth(%s, context);".formatted(column.getLargeWidth()));
                        JavaAdminUiCodeGenUtils.generateNestedComponent(column.getContent(), "content", gen);
                        gen.printLine("column.addChild(context, content, 0)");
                        gen.printLine("row.addChild(context, column, %s);".formatted(columnNumber.getAndIncrement()));
                    });
                });
                gen.printLine("%s.addChild(context, row, %s);".formatted(containerName, rowNumber.getAndIncrement()));
            });
        }
    }

    public static void collectFields(GridContainerDescription cd, Map<String, String> fields) {
        cd.getRows().forEach(r ->{
            r.getColumns().forEach(c ->{
                BuildExceptionUtils.wrapException(()->JavaAdminUiCodeGenUtils.collectFields(c.getContent(), fields));
            });
        });
    }
}
