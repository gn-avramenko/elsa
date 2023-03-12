/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.gradle.utils.BuildTextUtils;
import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.BaseElement;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.EnumItemDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class JavaCodeGeneratorUtils {

    public static String getPackage(String className) {
        var idx = className.lastIndexOf(".");
        if(idx == -1){
            throw new IllegalArgumentException("unable to calculate package name for \"%s\"".formatted(className));
        }
        return className.substring(0, idx);
    }

    public static String getSimpleName(String className) {
        if("int".equals(className) || "long".equals(className) || "boolean".equals(className) || "String".equals(className) ){
            return className;
        }
        var idx = className.lastIndexOf(".");
        return className.substring(idx + 1);
    }

    public static String getSimpleName(String className, JavaCodeGenerator gen) {
        if("int".equals(className) || "long".equals(className) || "boolean".equals(className) || "String".equals(className) ){
            return className;
        }
        if(!getPackage(className).equals(gen.getPackageName())){
            gen.addImport(className);
        }
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

    public static void processGenerics(List<GenericDescription> generics, int i, JavaCodeGenerator gen) throws Exception {
        gen.addImport("com.gridnine.elsa.meta.common.GenericDescription");
        gen.addImport("java.util.ArrayList");
        gen.printLine("var generics_%s = new ArrayList<GenericDescription>();".formatted(i));
        for(GenericDescription generic: generics){
            gen.wrapWithBlock("",()->{
                gen.printLine("var generic_%s = new GenericDescription();".formatted(i+1));
                gen.printLine("generic_%s.setId(\"%s\");".formatted(i+1, generic.getId()));
                gen.printLine("generic_%s.setType(\"%s\");".formatted(i+1,generic.getType()));
                if(generic.getObjectIdAttributeName() != null){
                    gen.printLine("generic_%s.setObjectIdAttributeName(\"%s\");".formatted(i+1, generic.getObjectIdAttributeName()));
                }
                if(!generic.getNestedGenerics().isEmpty()){
                    processGenerics(generic.getNestedGenerics(), i+2, gen);
                    gen.printLine("generic_%s.getNestedGenerics().addAll(generics_%s);".formatted(i+1, i+2));
                }
                gen.printLine("generics_%s.add(generic_%s);".formatted(i, i+1));
            });
        }
    }


    public static void generateBaseElementMetaRegistryConfiguratorCode(BaseElement ed, String elementName, JavaCodeGenerator gen) throws Exception {
        for(Map.Entry<String, String> entry : ed.getAttributes().entrySet()){
            gen.printLine("%s.getAttributes().put(\"%s\", \"%s\");".formatted(elementName, entry.getKey(), entry.getValue()));
        }
        for(Map.Entry<String, String> entry : ed.getParameters().entrySet()){
            gen.printLine("%s.getParameters().put(\"%s\", \"%s\");".formatted(elementName, entry.getKey(), entry.getValue()));
        }
        for(Map.Entry<Locale, String> entry : ed.getDisplayNames().entrySet()){
            gen.addImport("java.util.Locale");
            if(Locale.ROOT.equals(entry.getKey())){
                gen.printLine("%s.getDisplayNames().put(Locale.ROOT, \"%s\");".formatted(elementName, entry.getValue()));
            } else if (entry.getKey().getCountry() == null || "".equals(entry.getKey().getCountry())){
                gen.addImport("com.gridnine.elsa.core.utils.LocaleUtils");
                gen.printLine("%s.getDisplayNames().put(LocaleUtils.getLocale(\"%s\"), \"%s\");".formatted(elementName, entry.getKey().getLanguage(),  entry.getValue()));
            } else {
                gen.addImport("com.gridnine.elsa.core.utils.LocaleUtils");
                gen.printLine("%s.getDisplayNames().put(LocaleUtils.getLocale(\"%s\",\"%s\"), \"%s\");".formatted(elementName, entry.getKey().getLanguage(), entry.getKey().getCountry(), entry.getValue()));
            }
        }
    }

    public static void generateEnumMetaRegistryConfiguratorCode(EnumDescription ed, String smr, String cmr, JavaCodeGenerator gen) throws Exception {
        gen.wrapWithBlock("", ()->{
            gen.addImport("com.gridnine.elsa.meta.common.EnumDescription");
            gen.printLine("var enumDescription = new EnumDescription(\"%s\");".formatted(ed.getId()));
            generateBaseElementMetaRegistryConfiguratorCode(ed, "enumDescription", gen);
            for(EnumItemDescription ei: ed.getItems().values()){
                gen.wrapWithBlock("", ()->{
                    gen.addImport("com.gridnine.elsa.meta.common.EnumItemDescription");
                    gen.printLine("var enumItemDescription = new EnumItemDescription(\"%s\");".formatted(ei.getId()));
                    generateBaseElementMetaRegistryConfiguratorCode(ei, "enumItemDescription", gen);
                    gen.printLine("enumDescription.getItems().put(\"%s\", enumItemDescription);".formatted(ei.getId()));
                });
            }
            gen.printLine("%s.getEnums().put(\"%s\", enumDescription);".formatted(smr, ed.getId()));
            gen.printLine("%s.getEnumsIds().add(\"%s\");".formatted(cmr, ed.getId()));
        });
    }

    public static void generateEntityMetaRegistryConfiguratorCode(EntityDescription ed, String smr, String cmr, JavaCodeGenerator gen) throws Exception {
        gen.wrapWithBlock("", ()->{
            gen.addImport("com.gridnine.elsa.meta.common.EntityDescription");
            gen.printLine("var entityDescription = new EntityDescription(\"%s\");".formatted(ed.getId()));
            generateBaseElementMetaRegistryConfiguratorCode(ed, "entityDescription", gen);
            for(PropertyDescription prop: ed.getProperties().values()){
                gen.wrapWithBlock("", ()->{
                    gen.addImport("com.gridnine.elsa.meta.common.PropertyDescription");
                    gen.printLine("var propertyDescription = new PropertyDescription(\"%s\");".formatted(prop.getId()));
                    gen.printLine("propertyDescription.setTagName(\"%s\");".formatted(prop.getTagName()));
                    generateBaseElementMetaRegistryConfiguratorCode(prop, "propertyDescription", gen);
                    gen.printLine("entityDescription.getProperties().put(\"%s\", propertyDescription);".formatted(prop.getId()));
                });
            }
            gen.printLine("%s.getEntities().put(\"%s\", entityDescription);".formatted(smr, ed.getId()));
            gen.printLine("%s.add(\"%s\");".formatted(cmr, ed.getId()));
        });
    }


    public static void generateEnum(EnumDescription ed, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(getPackage(ed.getId()));
        gen.wrapWithBlock("public enum %s".formatted(getSimpleName(ed.getId())), () -> {
            var items = new ArrayList<>(ed.getItems().values());
            int size = items.size();
            for (int n = 0; n < size; n++) {
                gen.blankLine();
                var item = items.get(n);
                gen.printLine("%s%s".formatted(item.getId(), n == size - 1 ? "" : ","));
            }
        });
        var file = saveIfDiffers(gen.toString(), ed.getId() + ".java", destDir);
        generatedFiles.add(file);
    }

    public static void generateJavaEntityCode(GenEntityDescription ed, SerializableTypesRegistry sRegistry, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(ed.getId());
        gen.setPackageName(packageName);
        var extendsId = ed.getExtendsId();
        if (extendsId == null) {
            extendsId = "com.gridnine.elsa.core.model.common.BaseIntrospectableObject";
        }
        var extId1 = extendsId;
        var extId2 = extendsId;
        var index = extendsId.indexOf("<");
        if(index != -1){
            extId1 = extendsId.substring(0, index);
            extId2 = extendsId.substring(index+1, extendsId.length()-1);
        }
        if (!packageName.equals(getPackage(extId1))) {
            gen.addImport(extId1);
        }
        if (!packageName.equals(getPackage(extId2))) {
            gen.addImport(extId2);
        }
        String implementsId = null;
        if (ed.getToLocalizableStringExpression() != null) {
            implementsId = "com.gridnine.elsa.core.model.common.Localizable";
            gen.addImport("com.gridnine.elsa.core.model.common.L10nMessage");
        }
        if (implementsId != null && !implementsId.equals(getPackage(extendsId))) {
            gen.addImport(implementsId);
        }
        var cnsb = new StringBuilder();
        cnsb.append("public");
        if (ed.isAbstract()) {
            cnsb.append(" abstract");
        }
        cnsb.append(" class %s extends %s".formatted(getSimpleName(ed.getId(), gen),
                !extId1.equals(extId2)? "%s<%s>".formatted(getSimpleName(extId1, gen),
                        getSimpleName(extId2, gen))
                        : getSimpleName(extendsId, gen)));
        if (implementsId != null) {
            cnsb.append(" implements %s".formatted(getSimpleName(implementsId)));
        }
        gen.wrapWithBlock(cnsb.toString(), () -> {
            for(GenPropertyDescription property: ed.getProperties().values()){
                gen.blankLine();
                var type = calculateDeclarationType(property, sRegistry, gen);
                var st = sRegistry.getTypes().get(property.getTagDescription().getType());
                if(st != null && st.isFinalField()){
                     if(!st.getGenerics().isEmpty()){
                         gen.printLine("private final %s %s = new %s<>();".formatted(type, property.getId(), getSimpleName(st.getJavaQualifiedName(), gen)));
                     } else {
                         gen.printLine("private final %s %s = new %s();".formatted(type, property.getId(), getSimpleName(st.getJavaQualifiedName(), gen)));
                     }
                } else {
                    gen.printLine("private %s %s;".formatted(type, property.getId()));
                }
            }
            for(GenPropertyDescription property: ed.getProperties().values()){
                gen.blankLine();
                var type = calculateDeclarationType(property, sRegistry, gen);
                gen.wrapWithBlock("public %s get%s()".formatted(type, BuildTextUtils.capitalize(property.getId())),
                        () -> gen.printLine("return %s;".formatted(property.getId())));
                var st = sRegistry.getTypes().get(property.getTagDescription().getType());
                if(st == null || !st.isFinalField()){
                    gen.blankLine();
                    gen.wrapWithBlock("public void set%s(%s value)".formatted(
                            BuildTextUtils.capitalize(property.getId()),type),
                            () -> gen.printLine("this.%s = value;".formatted(property.getId())));
                }
            }
            if (!ed.getProperties().isEmpty()) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public Object getValue(String propertyName)", () -> {
                    for (var pd : ed.getProperties().values()) {
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(propertyName))".formatted(pd.getId()), () -> gen.printLine("return %s;".formatted(pd.getId())));
                    }
                    gen.blankLine();
                    gen.printLine("return super.getValue(propertyName);");
                });
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public void setValue(String propertyName, Object value)", () -> {
                    for (var pd : ed.getProperties().values()) {
                        var st = sRegistry.getTypes().get(pd.getTagDescription().getType());
                        if(st != null && st.isFinalField()){
                           continue;
                        }
                        gen.blankLine();
                        var type = calculateDeclarationType(pd, sRegistry, gen);
                        gen.wrapWithBlock("if(\"%s\".equals(propertyName))".formatted(pd.getId()), () -> {
                            gen.printLine("this.%s = (%s) value;".formatted(pd.getId(), type));
                            gen.printLine("return;");
                        });
                    }
                    gen.blankLine();
                    gen.printLine("super.setValue(propertyName, value);");
                });
            }
            if (ed.getToLocalizableStringExpression() != null) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.addImport("java.util.*");
                gen.wrapWithBlock("public String toString(Locale locale)", () -> gen.printLine("return %s;".formatted(ed.getToLocalizableStringExpression())));
            }
            if (ed.getToStringExpression() != null) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public String toString()", () -> gen.printLine("return %s;".formatted(ed.getToStringExpression())));
            }
        });

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), ed.getId() + ".java", destDir);
        generatedFiles.add(file);
    }

    public static String calculateDeclarationType(GenPropertyDescription property, SerializableTypesRegistry sRegistry, JavaCodeGenerator gen) {
        String typeStr = property.getTagDescription().getType();
        if("ENTITY".equals(typeStr) || "ENUM".equals(typeStr) ){
            return getSimpleName(property.getAttributes().get("class-name"), gen);
        }
        var type = sRegistry.getTypes().get(typeStr);
        if(type.getGenerics().isEmpty()){
            if(type.getJavaQualifiedName().contains(".")){
                gen.addImport(type.getJavaQualifiedName());
                return getSimpleName(type.getJavaQualifiedName());
            }
            return type.getJavaQualifiedName();
        }
        var qn = type.getJavaQualifiedName();
        if(qn.contains(".")){
            gen.addImport(type.getJavaQualifiedName());
            qn = getSimpleName(qn);
        }
        var typeBuilder = new StringBuilder(qn);
        typeBuilder.append("<");
        processGenerics(type.getGenerics(), property, property.getTagDescription().getGenerics(), sRegistry, typeBuilder, gen);
        typeBuilder.append(">");
        return typeBuilder.toString();
    }

    private static void processGenerics(List<GenericDeclaration> typesGenerics, GenPropertyDescription property, List<GenericDescription> tagGenerics, SerializableTypesRegistry stReg, StringBuilder typeBuilder, JavaCodeGenerator gen) {
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
                    typeBuilder.append(getSimpleName(ngt.getJavaQualifiedName(), gen));
                    continue;
                }
                var type2 = stReg.getTypes().get(tagGeneric.getType());
                typeBuilder.append("%s<".formatted(getSimpleName(type2.getJavaQualifiedName(), gen)));
                processGenerics(type2.getGenerics(), property, tagGeneric.getNestedGenerics(), stReg, typeBuilder, gen);
                typeBuilder.append(">");
                continue;
            }
            processGenerics(((GenericsDeclaration) generic).getGenerics(), property, tagGenerics, stReg, typeBuilder, gen);
        }
    }

    public static<T extends TagDescription> GenEntityDescription buildGenEntityDescription(String entityId, SerializableMetaRegistry sRegistry, Map<String, T> entityTags) {
        var ged = new GenEntityDescription();
        ged.setId(entityId);
        var ed = sRegistry.getEntities().get(entityId);
        ged.setAbstract("true".equals(ed.getAttributes().get("abstract")));
        ged.setExtendsId(ed.getAttributes().get("extends"));
        for(PropertyDescription prop: ed.getProperties().values()){
            var gpd = new GenPropertyDescription();
            var td = entityTags.get(prop.getTagName());
            if(td == null){
                td = (T) new TagDescription();
                td.setType("ENTITY");
                td.setTagName(prop.getTagName());
            }
            gpd.setTagDescription(td);
            gpd.setId(prop.getId());
            gpd.setTagName(prop.getTagName());
            gpd.getAttributes().putAll(prop.getAttributes());
            ged.getProperties().put(prop.getId(), gpd);
        }
        return ged;
    }
}
