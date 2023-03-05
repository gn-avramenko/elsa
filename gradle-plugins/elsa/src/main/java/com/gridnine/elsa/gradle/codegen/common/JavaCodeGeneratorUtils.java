/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class JavaCodeGeneratorUtils {

    public static String getPackage(String className) {
        var idx = className.lastIndexOf(".");
        return className.substring(0, idx);
    }

    public static String getSimpleName(String className) {
        var idx = className.lastIndexOf(".");
        return className.substring(idx + 1);
    }

    public static File saveIfDiffers(String content, String fileName, File destDir) throws IOException {
        var parts = fileName.split("\\.");
        var currentFile = destDir;
        var length = parts.length;
        for (int n = 0; n < length - 2; n++) {
            currentFile = new File(currentFile, parts[n] + "/");
            if(!currentFile.exists()) {currentFile.mkdirs();};
        }
        currentFile = new File(currentFile, parts[parts.length - 2] + "." + parts[parts.length - 1]);
        if (currentFile.exists()) {
            var currentContent = Files.readString(currentFile.toPath(), StandardCharsets.UTF_8);
            if (currentContent.equals(content)) {
                return currentFile;
            }
        }
        currentFile.getParentFile().mkdirs();
        while (!currentFile.getParentFile().exists()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //noops
            }
            currentFile.getParentFile().mkdirs();
        }
        Files.writeString(currentFile.toPath(), content);
        return currentFile;
    }

    public static void generateAttributeDescription(JavaCodeGenerator gen, AttributeDescription attr, String mapName) throws Exception {
        gen.wrapWithBlock("", ()->{
            gen.addImport("com.gridnine.elsa.meta.common.AttributeDescription");
            gen.addImport("com.gridnine.elsa.meta.common.AttributeType");
            gen.printLine("var attr = new AttributeDescription();");
            gen.printLine("attr.setName(\"%s\");".formatted(attr.getName()));
            gen.printLine("attr.setType(AttributeType.%s);".formatted(attr.getType().name()));
            if(attr.getDefaultValue() != null){
                gen.printLine("attr.setDefaultValue(\"%s\");".formatted(attr.getDefaultValue()));
            }
            for(String sv: attr.getSelectValues()){
                gen.printLine("attr.getSelectValues().add(\"%s\");".formatted(sv));
            }
            gen.printLine("%s.put(\"%s\", attr);".formatted(mapName, attr.getName()));
        });
    }

    public static void generateTagDescription(JavaCodeGenerator gen, TagDescription tag, String mapName) throws Exception {
        gen.wrapWithBlock("", ()->{
            gen.addImport("com.gridnine.elsa.meta.common.TagDescription");
            gen.printLine("var tag = new TagDescription();");
            gen.printLine("tag.setTagName(\"%s\");".formatted(tag.getTagName()));
            gen.printLine("tag.setType(\"%s\");".formatted(tag.getType()));
            if(tag.getObjectIdAttributeName() != null){
                gen.printLine("tag.setObjectIdAttributeName(\"%s\");".formatted(tag.getObjectIdAttributeName()));
            }
            gen.printLine("tag.setType(\"%s\");".formatted(tag.getType()));
            for(AttributeDescription attr: tag.getAttributes().values()){
                generateAttributeDescription(gen, attr, "tag.getAttributes()");
            }
            if(!tag.getGenerics().isEmpty()){
                processGenerics(tag.getGenerics(), 0, gen);
                gen.printLine("tag.getGenerics().addAll(generics_0);");
            }
            gen.printLine("%s.put(\"%s\", tag);".formatted(mapName, tag.getTagName()));
        });
    }

    private static void processGenerics(List<GenericDescription> generics, int i, JavaCodeGenerator gen) throws Exception {
        gen.addImport("com.gridnine.elsa.meta.common.GenericDescription");
        gen.addImport("java.util.ArrayList");
        gen.printLine("var generics_%s = new ArrayList<GenericDescription>();".formatted(i));
        for(GenericDescription generic: generics){
            gen.wrapWithBlock("",()->{
                gen.printLine("var generic = new GenericDescription();");
                gen.printLine("generic.setId(\"%s\");".formatted(generic.getId()));
                gen.printLine("generic.setType(\"%s\");".formatted(generic.getType()));
                if(generic.getObjectIdAttributeName() != null){
                    gen.printLine("generic.setObjectIdAttributeName(\"%s\");".formatted(generic.getObjectIdAttributeName()));
                }
                if(!generic.getNestedGenerics().isEmpty()){
                    processGenerics(generic.getNestedGenerics(), i+1, gen);
                }
                gen.printLine("generics_%s.add(generic);".formatted(i));
            });
        }
    }
}
