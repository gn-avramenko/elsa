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

import com.gridnine.platform.elsa.gradle.codegen.adminUi.common.JavaAdminUiCodeGenUtils;
import com.gridnine.platform.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormContainerDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormCustomElementDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormRemoteMultiSelectDescription;
import com.gridnine.platform.elsa.gradle.meta.adminUi.form.FormRemoteSelectDescription;
import com.gridnine.platform.elsa.gradle.utils.BuildRunnableWithException;

public class JavaAdminUiFormConfiguratorHelper {

    public static void generateDescription(FormContainerDescription cd, String containerDescriptionName, boolean root, JavaCodeGenerator gen) throws Exception {
        gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormContainerDescription");
        var runnable = new BuildRunnableWithException(){

            @Override
            public void run() throws Exception {
                    if(root) {
                        gen.printLine("""
                        var %s = (FormContainerDescription) registry.getContainers().computeIfAbsent("%s", className->{
                                        var ctr = new FormContainerDescription();
                                        ctr.setClassName(className);
                                        return ctr;
                                    });""".formatted(containerDescriptionName, cd.getClassName()));
                    } else {
                        gen.printLine("var %s = new FormContainerDescription();".formatted(containerDescriptionName));
                    }
                    for(var comp : cd.getComponents().values()){
                        switch (comp.getType()) {
                            case TEXT_FIELD -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormTextFieldDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormTextFieldDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                            case BOOLEAN_FIELD -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormBooleanFieldDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormBooleanFieldDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                            case TEXT_AREA -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormTextAreaDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormTextAreaDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                            case DATE_INTERVAL_FIELD -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormDateIntervalFieldDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormDateIntervalFieldDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                            case REMOTE_SELECT -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormRemoteSelectDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormRemoteSelectDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    gen.printLine("comp.setClassName(\"%s\");".formatted(((FormRemoteSelectDescription)comp).getClassName()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });

                            }
                            case REMOTE_MULTI_SELECT -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormRemoteMultiSelectDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormRemoteMultiSelectDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    gen.printLine("comp.setClassName(\"%s\");".formatted(((FormRemoteMultiSelectDescription)comp).getClassName()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                            case SELECT -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormSelectDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormSelectDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });

                            }
                            case CUSTOM -> {
                                gen.addImport("com.gridnine.platform.elsa.common.meta.adminUi.form.FormCustomElementDescription");
                                gen.wrapWithBlock(null, () -> {
                                    gen.printLine("var comp = new FormCustomElementDescription();");
                                    gen.printLine("comp.setId(\"%s\");".formatted(comp.getId()));
                                    JavaAdminUiCodeGenUtils.updateTitle(comp.getTitle(), gen);
                                    gen.printLine("comp.setClassName(\"%s\");".formatted(((FormCustomElementDescription) comp).getClassName()));
                                    gen.printLine("%s.getComponents().put(comp.getId(), comp);".formatted(containerDescriptionName));
                                });
                            }
                        }
                    }
            }
        };
        if(root) {
            gen.wrapWithBlock(null,runnable);
        } else {
            runnable.run();
        }
    }
}
