/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.common.meta.common.*;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CodeGeneratorUtils {

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
            assert currentFile.exists() || currentFile.mkdirs();
        }
        currentFile = new File(currentFile, parts[parts.length - 2] + "." + parts[parts.length - 1]);
        if (currentFile.exists()) {
            var currentContent = Files.readString(currentFile.toPath(), StandardCharsets.UTF_8);
            if (currentContent.equals(content)) {
                return currentFile;
            }
        }
        assert currentFile.getParentFile().mkdirs();
        Files.writeString(currentFile.toPath(), content);
        return currentFile;
    }

    public static void generateJavaEntityConfiguratorCode(EntityDescription ed, JavaCodeGenerator gen) throws Exception {
        gen.addImport("com.gridnine.elsa.common.meta.common.EntityDescription");
        gen.wrapWithBlock(null, () -> {
            gen.printLine("var entityDescription = new EntityDescription(\"%s\");".formatted(ed.getId()));
            gen.printLine("registry.getEntities().put(entityDescription.getId(), entityDescription);");
            generateJavaEntityConfiguratorCode("entityDescription", ed, gen);
        });
    }

    public static void generateJavaEnumConfiguratorCode(EnumDescription ed, JavaCodeGenerator gen) throws Exception {
        gen.addImport("com.gridnine.elsa.common.meta.common.EnumDescription");
        gen.wrapWithBlock(null, () -> {
            gen.printLine("var enumDescription = new EnumDescription(\"%s\");".formatted(ed.getId()));
            gen.printLine("registry.getEnums().put(enumDescription.getId(), enumDescription);");
            for (EnumItemDescription eid : ed.getItems().values()) {
                gen.addImport("com.gridnine.elsa.common.meta.common.EnumItemDescription");
                gen.wrapWithBlock(null, () -> {
                    gen.printLine("var enumItemDescription = new EnumItemDescription(\"%s\");".formatted(eid.getId()));
                    for (Map.Entry<Locale, String> entry : eid.getDisplayNames().entrySet()) {
                        gen.addImport("com.gridnine.elsa.common.core.utils.LocaleUtils");
                        gen.printLine("enumItemDescription.getDisplayNames().put(LocaleUtils.getLocale(\"%s\",\"%s\"), \"%s\");".formatted(entry.getKey().getLanguage(), entry.getKey().getCountry(), entry.getValue()));
                    }
                    gen.printLine("enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);");
                });
            }
        });
    }

    public static void generateJavaEntityConfiguratorCode(String descriptionName, EntityDescription ed, JavaCodeGenerator gen) throws Exception {
        if (ed.isAbstract()) {
            gen.printLine("%s.setAbstract(true);".formatted(descriptionName));
        }
        if (ed.getExtendsId() != null) {
            gen.printLine("%s.setExtendsId(\"%s\");".formatted(descriptionName, ed.getExtendsId()));
        }
        for (StandardPropertyDescription pd : ed.getProperties().values()) {
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardPropertyDescription");
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardValueType");
            gen.wrapWithBlock(null, () -> {
                gen.printLine("var propertyDescription = new StandardPropertyDescription(\"%s\");".formatted(pd.getId()));
                gen.printLine("propertyDescription.setType(StandardValueType.%s);".formatted(pd.getType().name()));
                if (pd.getClassName() != null) {
                    gen.printLine("propertyDescription.setClassName(\"%s\");".formatted(pd.getClassName()));
                }
                if (pd.isNullable()) {
                    gen.printLine("propertyDescription.setNullable(true);");
                }
                gen.printLine("%s.getProperties().put(propertyDescription.getId(), propertyDescription);".formatted(descriptionName));
            });
        }
        for (StandardCollectionDescription cd : ed.getCollections().values()) {
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardCollectionDescription");
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardValueType");
            gen.wrapWithBlock(null, () -> {
                gen.printLine("var collectionDescription = new StandardCollectionDescription(\"%s\");".formatted(cd.getId()));
                gen.printLine("collectionDescription.setElementType(StandardValueType.%s);".formatted(cd.getElementType().name()));
                if (cd.getElementClassName() != null) {
                    gen.printLine("collectionDescription.setElementClassName(\"%s\");".formatted(cd.getElementClassName()));
                }
                if (cd.isUnique()) {
                    gen.printLine("collectionDescription.setUnique(true);");
                }
                gen.printLine("%s.getCollections().put(collectionDescription.getId(), collectionDescription);".formatted(descriptionName));
            });
        }
        for (StandardMapDescription md : ed.getMaps().values()) {
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardMapDescription");
            gen.addImport("com.gridnine.elsa.common.meta.common.StandardValueType");
            gen.wrapWithBlock(null, () -> {
                gen.printLine("var mapDescription = new StandardMapDescription(\"%s\");".formatted(md.getId()));
                gen.printLine("mapDescription.setKeyType(StandardValueType.%s);".formatted(md.getKeyType().name()));
                gen.printLine("mapDescription.setValueType(StandardValueType.%s);".formatted(md.getValueType().name()));
                if (md.getKeyClassName() != null) {
                    gen.printLine("mapDescription.setKeyClassName(\"%s\");".formatted(md.getKeyClassName()));
                }
                if (md.getValueClassName() != null) {
                    gen.printLine("mapDescription.setValueClassName(\"%s\");".formatted(md.getValueClassName()));
                }
                gen.printLine("%s.getMaps().put(mapDescription.getId(), mapDescription);".formatted(descriptionName));
            });
        }

    }

    public static void generateJavaEnumCode(EnumDescription ed, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        gen.setPackageName(CodeGeneratorUtils.getPackage(ed.getId()));
        gen.wrapWithBlock("public enum %s".formatted(CodeGeneratorUtils.getSimpleName(ed.getId())), () -> {
            var items = new ArrayList<>(ed.getItems().values());
            int size = items.size();
            for (int n = 0; n < size; n++) {
                gen.blankLine();
                var item = items.get(n);
                gen.printLine("%s%s".formatted(item.getId(), n == size - 1 ? "" : ","));
            }
        });
        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), ed.getId() + ".java", destDir);
        generatedFiles.add(file);
    }

    public static void generateJavaEntityCode(EntityDescription ed, File destDir, Set<File> generatedFiles) throws Exception {
        var ged = new GenEntityDescription();
        ged.setAbstract(ed.isAbstract());
        ged.setExtendsId(ed.getExtendsId());
        ged.setId(ed.getId());
        ged.getProperties().putAll(ed.getProperties());
        ged.getCollections().putAll(ed.getCollections());
        ged.getMaps().putAll(ed.getMaps());
        generateJavaEntityCode(ged, destDir, generatedFiles);
    }

    public static void generateJavaEntityCode(GenEntityDescription ed, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = CodeGeneratorUtils.getPackage(ed.getId());
        gen.setPackageName(packageName);
        var extendsId = ed.getExtendsId();
        if (extendsId == null) {
            extendsId = "com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject";
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
            implementsId = "com.gridnine.elsa.common.core.model.common.Localizable";
            gen.addImport("com.gridnine.elsa.common.core.model.common.L10nMessage");
        }
        if (implementsId != null && !implementsId.equals(getPackage(extendsId))) {
            gen.addImport(implementsId);
        }
        var cnsb = new StringBuilder();
        cnsb.append("public");
        if (ed.isAbstract()) {
            cnsb.append(" abstract");
        }
        cnsb.append(" class %s extends %s".formatted(CodeGeneratorUtils.getSimpleName(ed.getId()),
                !extId1.equals(extId2)? "%s<%s>".formatted(CodeGeneratorUtils.getSimpleName(extId1),
                        CodeGeneratorUtils.getSimpleName(extId2))
                        : CodeGeneratorUtils.getSimpleName(extendsId)));
        if (implementsId != null) {
            cnsb.append(" implements %s".formatted(CodeGeneratorUtils.getSimpleName(implementsId)));
        }
        gen.wrapWithBlock(cnsb.toString(), () -> {
            for (StandardPropertyDescription pd : ed.getProperties().values()) {
                gen.blankLine();
                String className = getPropertyType(pd.getType(), pd.getClassName(), pd.isNullable(), gen);
                gen.printLine("private %s %s;".formatted(className, pd.getId()));
            }
            for (StandardCollectionDescription cd : ed.getCollections().values()) {
                gen.blankLine();
                String className = getPropertyType(cd.getElementType(), cd.getElementClassName(), false, gen);
                gen.addImport("java.util.*");
                gen.printLine("private final %s<%s> %s = new %s<>();".formatted(cd.isUnique() ? "Set" : "List", className, cd.getId(), cd.isUnique() ? "HashSet" : "ArrayList"));
            }
            for (StandardMapDescription md : ed.getMaps().values()) {
                gen.blankLine();
                gen.addImport("java.util.*");
                String keyClassName = getPropertyType(md.getKeyType(), md.getKeyClassName(), false, gen);
                String valueClassName = getPropertyType(md.getValueType(), md.getValueClassName(), false, gen);
                gen.printLine("private final Map<%s,%s> %s = new HashMap<>();".formatted(keyClassName, valueClassName, md.getId()));
            }
            for (StandardPropertyDescription pd : ed.getProperties().values()) {
                gen.blankLine();
                String className = getPropertyType(pd.getType(), pd.getClassName(), pd.isNullable(), gen);
                gen.wrapWithBlock("public %s get%s()".formatted(className, BuildTextUtils.capitalize(pd.getId())), () -> gen.printLine("return %s;".formatted(pd.getId())));
                gen.blankLine();
                gen.wrapWithBlock("public void set%s(%s value)".formatted(BuildTextUtils.capitalize(pd.getId()),className), () -> gen.printLine("this.%s = value;".formatted(pd.getId())));
            }
            for (StandardCollectionDescription cd : ed.getCollections().values()) {
                gen.blankLine();
                gen.addImport("java.util.*");
                String className = getPropertyType(cd.getElementType(), cd.getElementClassName(), false, gen);
                gen.wrapWithBlock("public %s<%s> get%s()".formatted(cd.isUnique()? "Set" : "List", className, BuildTextUtils.capitalize(cd.getId())), () -> gen.printLine("return %s;".formatted(cd.getId())));
            }
            for (StandardMapDescription md : ed.getMaps().values()) {
                gen.blankLine();
                gen.addImport("java.util.*");
                String keyClassName = getPropertyType(md.getKeyType(), md.getKeyClassName(), false, gen);
                String valueClassName = getPropertyType(md.getValueType(), md.getValueClassName(), false, gen);
                gen.wrapWithBlock("public Map<%s,%s> get%s()".formatted(keyClassName, valueClassName, BuildTextUtils.capitalize(md.getId())), () -> gen.printLine("return %s;".formatted(md.getId())));
            }
            if (!ed.getProperties().isEmpty()) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public Object getValue(String propertyName)", () -> {
                    for (StandardPropertyDescription pd : ed.getProperties().values()) {
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(propertyName))".formatted(pd.getId()), () -> gen.printLine("return %s;".formatted(pd.getId())));
                    }
                    gen.blankLine();
                    gen.printLine("return super.getValue(propertyName);");
                });
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public void setValue(String propertyName, Object value)", () -> {
                    for (StandardPropertyDescription pd : ed.getProperties().values()) {
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(propertyName))".formatted(pd.getId()), () -> {
                            if(pd.getType() == StandardValueType.ENTITY_REFERENCE){
                                gen.printLine("//noinspection unchecked");
                            }
                            gen.printLine("this.%s = (%s) value;".formatted(pd.getId(), getPropertyType(pd.getType(), pd.getClassName(), pd.isNullable(), gen)));
                            gen.printLine("return;");
                        });
                    }
                    gen.blankLine();
                    gen.printLine("super.setValue(propertyName, value);");
                });
            }
            if (!ed.getCollections().isEmpty()) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public Collection<?> getCollection(String collectionName)", () -> {
                    for (StandardCollectionDescription cd : ed.getCollections().values()) {
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(collectionName))".formatted(cd.getId()), () -> gen.printLine("return %s;".formatted(cd.getId())));
                    }
                    gen.blankLine();
                    gen.printLine("return super.getCollection(collectionName);");
                });
            }
            if (!ed.getMaps().isEmpty()) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public Map<?,?> getMap(String mapName)", () -> {
                    for (StandardMapDescription md : ed.getMaps().values()) {
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(mapName))".formatted(md.getId()), () -> gen.printLine("return %s;".formatted(md.getId())));
                    }
                    gen.blankLine();
                    gen.printLine("return super.getMap(mapName);");
                });
            }
            if (ed.getToLocalizableStringExpression() != null) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.addImport("java.util.*");
                gen.wrapWithBlock("public String toString(Locale locale)", () -> gen.printLine(ed.getToLocalizableStringExpression()));
            }
            if (ed.getToStringExpression() != null) {
                gen.blankLine();
                gen.printLine("@Override");
                gen.wrapWithBlock("public String toString()", () -> gen.printLine(ed.getToStringExpression()));
            }
        });

        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), ed.getId() + ".java", destDir);
        generatedFiles.add(file);
    }

    public static String getPropertyType(StandardValueType type, String className, boolean nullable, JavaCodeGenerator gen) {
        return switch (type) {
            case STRING, CLASS -> "String";
            case LOCAL_DATE -> {
                gen.addImport(LocalDate.class.getName());
                yield LocalDate.class.getSimpleName();
            }
            case LOCAL_DATE_TIME -> {
                gen.addImport(LocalDateTime.class.getName());
                yield LocalDateTime.class.getSimpleName();
            }
            case BOOLEAN -> nullable ? "Boolean" : "boolean";
            case BYTE_ARRAY -> "byte[]";
            case ENUM, ENTITY -> {
                var pk = getPackage(className);
                if (!gen.getPackageName().equals(pk)) {
                    gen.addImport(className);
                }
                yield getSimpleName(className);
            }

            case ENTITY_REFERENCE -> {
                var pk = getPackage(className);
                if (!gen.getPackageName().equals(pk)) {
                    gen.addImport(className);
                }
                gen.addImport("com.gridnine.elsa.common.core.model.domain.EntityReference");
                yield "EntityReference<%s>".formatted(CodeGeneratorUtils.getSimpleName(className));
            }
            case LONG -> nullable ? "Long" : "long";
            case INT -> nullable ? "Integer" : "int";
            case BIG_DECIMAL -> {
                gen.addImport(BigDecimal.class.getName());
                yield BigDecimal.class.getSimpleName();
            }
        };
    }
}
