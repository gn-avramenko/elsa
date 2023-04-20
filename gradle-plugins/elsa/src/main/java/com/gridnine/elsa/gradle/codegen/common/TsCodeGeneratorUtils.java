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

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.serialization.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TsCodeGeneratorUtils {

    public static void generateWebEnumCode(EnumDescription ed, TypeScriptCodeGenerator gen) {
        gen.printLine("export type %s=".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId())));
        var items = new ArrayList<>(ed.getItems().values());
        for(int n =0;n < items.size(); n++ ){
            if(n == 0){
                gen.printLine("'%s'".formatted(items.get(n).getId()));
            }  else {
                gen.printLine("| '%s'".formatted(items.get(n).getId()));
            }
        }
        gen.print(";\n");
    }

    public static File saveIfDiffers(String content, File module) throws IOException {
        if(module.exists()) {
            var currentContent = Files.readString(module.toPath(), StandardCharsets.UTF_8);
            if (currentContent.equals(content)) {
                return module;
            }
        }
        if(!module.getParentFile().exists()){
            module.getParentFile().mkdirs();
        }
        Files.writeString(module.toPath(), content);
        return module;
    }

    public static void generateWebEntityCode(GenEntityDescription ed,  SerializableTypesRegistry sRegistry, TypeScriptCodeGenerator gen) throws Exception {
        gen.wrapWithBlock("export type %s=".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId())), () ->{
            for(var pd: ed.getProperties().values()){
                gen.printLine("%s%s: %s,".formatted(pd.getId(), isNullable(pd, sRegistry)? "?": "", calculateDeclarationType(pd, sRegistry, gen)));
            }
        });
        gen.print(";\n");
    }

    public static String calculateDeclarationType(GenPropertyDescription property, SerializableTypesRegistry sRegistry, TypeScriptCodeGenerator gen) {
        String typeStr = property.getTagDescription().getType();
        if("ENTITY".equals(typeStr) || "ENUM".equals(typeStr) ){
            return getSimpleName(property.getAttributes().get("class-name"), gen);
        }
        var type = sRegistry.getTypes().get(typeStr);
        if(type.getTsGenerics().isEmpty()){
            return getSimpleName(type.getTsQualifiedName(), gen);
        }
        var qn = type.getTsQualifiedName();
        if(qn.contains(".")){
            qn = getSimpleName(qn, gen);
        }
        if("[]".equals(type.getTsQualifiedName())){
            var typeBuilder = new StringBuilder("");
            processGenerics(type.getTsGenerics(), property, property.getTagDescription().getGenerics(), sRegistry, typeBuilder, gen);
            typeBuilder.append("[]");
            return typeBuilder.toString();
        }
        var typeBuilder = new StringBuilder(qn);
        typeBuilder.append("<");
        processGenerics(type.getTsGenerics(), property, property.getTagDescription().getGenerics(), sRegistry, typeBuilder, gen);
        typeBuilder.append(">");
        return typeBuilder.toString();
    }

    public static String getSimpleName(String className, TypeScriptCodeGenerator gen) {
        if("int".equals(className) || "long".equals(className)){
            return "number";
        }
        if("boolean".equals(className)){
            return "boolean";
        }
        if("String".equals(className) ){
            return "string";
        }
        if(className.contains(":")){
            gen.getTsImports().add(className);
            int idx = className.lastIndexOf("/");
            if(idx == -1){
                idx = className.lastIndexOf(":");
            }
            return className.substring(idx+1);
        } else {
            gen.getJavaImports().add(className);
        }
        var idx = className.lastIndexOf(".");
        return className.substring(idx + 1);
    }

    private static void processGenerics(List<GenericDeclaration> typesGenerics, GenPropertyDescription property, List<GenericDescription> tagGenerics, SerializableTypesRegistry stReg, StringBuilder typeBuilder, TypeScriptCodeGenerator gen) {
        for(int n = 0; n < typesGenerics.size(); n++){
            if(n >0){
                typeBuilder.append(", ");
            }
            var generic = typesGenerics.get(n);
            if(generic instanceof SingleGenericDeclaration sgd){
                var genId = sgd.getId();
                var tagGeneric = tagGenerics.stream().filter(it -> it.getId().equals(sgd.getId())).findFirst().get();
                if("ENTITY".equals(tagGeneric.getType()) || "ENUM".equals(tagGeneric.getType())){
                    typeBuilder.append(getSimpleName(property.getAttributes().get(tagGeneric.getObjectIdAttributeName()), gen));
                    continue;
                }
                if(tagGeneric.getNestedGenerics().isEmpty()){
                    var ngt = stReg.getTypes().get(tagGeneric.getType());
                    typeBuilder.append(getSimpleName(ngt.getTsQualifiedName(), gen));
                    continue;
                }
                var type2 = stReg.getTypes().get(tagGeneric.getType());
                if(type2.getTsGenerics().isEmpty()){
                    typeBuilder.append(getSimpleName(type2.getTsQualifiedName(), gen));
                } else {
                    typeBuilder.append("%s<".formatted(getSimpleName(type2.getTsQualifiedName(), gen)));
                    processGenerics(type2.getTsGenerics(), property, tagGeneric.getNestedGenerics(), stReg, typeBuilder, gen);
                    typeBuilder.append(">");
                }
                continue;
            }
            processGenerics(((GenericsDeclaration) generic).getGenerics(), property, tagGenerics, stReg, typeBuilder, gen);
        }
    }

//    public static void generateWebEntityCode(EntityDescription ed, TypeScriptCodeGenerator gen) throws Exception {
//        gen.wrapWithBlock("export type %s=".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId())), () ->{
//            for(var pd: ed.getProperties().values()){
//                gen.printLine("%s%s: %s,".formatted(pd.getId(), isNullable(pd)? "?": "", getType(pd.getType(), pd.getClassName())));
//            }
//            for(var cd: ed.getCollections().values()){
//                gen.printLine("%s: %s[],".formatted(cd.getId(),  getType(cd.getElementType(), cd.getElementClassName())));
//            }
//            for(var md: ed.getMaps().values()){
//                gen.printLine("%s: Map<%s, %s>,".formatted(md.getId(),  getType(md.getKeyType(), md.getKeyClassName()),
//                        getType(md.getValueType(), md.getValueClassName())));
//            }
//        });
//        gen.print(";\n");
//    }
//
//    public static String getType(StandardValueType vt, String className) {
//        return switch (vt){
//            case LONG,INT,BIG_DECIMAL -> "number";
//            case STRING,CLASS -> "string";
//            case LOCAL_DATE, LOCAL_DATE_TIME -> "Date";
//            case ENTITY,  ENUM -> JavaCodeGeneratorUtils.getSimpleName(className);
//            case BOOLEAN -> "boolean";
//            case BYTE_ARRAY -> "Uint8Array";
//            case ENTITY_REFERENCE -> "EntityReference";
//        };
//    }
//
    private static boolean isNullable(GenPropertyDescription pd,SerializableTypesRegistry str) {
        if(pd.isNonNullable()){
            return false;
        }
        SerializableType st = str.getTypes().get(pd.getTagDescription().getType());
        if(st != null && st.isFinalField()){
            return false;
        }
        return true;
    }
//
//    public static void generateImportCode(Collection<EntityDescription> values, Set<String> additionalEntities, Map<String, File> tsa, TypeScriptCodeGenerator gen, File file) throws Exception{
//        Set<String> entities = new LinkedHashSet<>();
//        values.forEach(ett -> {
//            ett.getProperties().values().forEach(prop ->{
//                if(prop.getType() == StandardValueType.ENTITY || prop.getType() == StandardValueType.ENUM ){
//                    entities.add(prop.getClassName());
//                }
//            });
//            ett.getCollections().values().forEach(coll ->{
//                if(coll.getElementType() == StandardValueType.ENTITY || coll.getElementType() == StandardValueType.ENUM ){
//                    entities.add(coll.getElementClassName());
//                }
//            });
//            ett.getMaps().values().forEach(map ->{
//                if(map.getKeyType() == StandardValueType.ENTITY || map.getKeyType() == StandardValueType.ENUM ){
//                    entities.add(map.getKeyClassName());
//                }
//                if(map.getValueType() == StandardValueType.ENTITY || map.getValueType() == StandardValueType.ENUM ){
//                    entities.add(map.getValueClassName());
//                }
//            });
//        });
//        entities.addAll(additionalEntities);
//        var imports = new LinkedHashMap<String, Set<String>>();
//        for(String clsName: entities){
//            var sf = tsa.get(clsName);
//            if(sf != null && !sf.equals(file)){
//                String relPath;
//                if(sf.getParentFile().equals(file.getParentFile())){
//                    relPath = "./%s".formatted(sf.getName());
//                } else {
//                    relPath = file.getParentFile().toPath().relativize(sf.toPath()).toString();
//                }
//                relPath = relPath.substring(0, relPath.lastIndexOf('.'));
//                imports.computeIfAbsent(relPath, (it) -> new LinkedHashSet<>()).add(JavaCodeGeneratorUtils.getSimpleName(clsName));
//            }
//        }
//        if(imports.isEmpty()){
//            return;
//        }
//        for(Map.Entry<String, Set<String>> entry : imports.entrySet()){
//            gen.wrapWithBlock("import ", () -> entry.getValue().forEach(value -> gen.printLine("%s,".formatted(value))));
//            gen.print(" from '%s';".formatted(entry.getKey()));
//        }
//        gen.blankLine();
//
//    }
}
