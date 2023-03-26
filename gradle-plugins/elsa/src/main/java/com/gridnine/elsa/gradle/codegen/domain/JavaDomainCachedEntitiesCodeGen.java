/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.gradle.codegen.common.GenEntityDescription;
import com.gridnine.elsa.gradle.codegen.common.GenPropertyDescription;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGeneratorUtils;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaDomainCachedEntitiesCodeGen {
    public void generate(DomainMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, DomainTypesRegistry dtReg, File destDir, Set<File> generatedFiles) throws Exception {
        Set<String> cachedObjects = new HashSet<>();
        registry.getDocumentsIds().forEach((id) ->{
            var doc = sRegistry.getEntities().get(id);
            if("true".equals(doc.getAttributes().get("cache-resolve"))){
                collectObjects(id, sRegistry, dtReg, cachedObjects);
            }
        });
        registry.getAssetsIds().forEach((id) ->{
            var asset = sRegistry.getEntities().get(id);
            if("true".equals(asset.getAttributes().get("cache-resolve"))){
                collectObjects(id, sRegistry, dtReg, cachedObjects);
            }
        });

        registry.getEntitiesIds().forEach(id -> {
            if (isCached(id, cachedObjects, sRegistry) && !isAbstract(id, sRegistry)) {
                BuildExceptionUtils.wrapException(() -> {
                    generateJavaCode(id, sRegistry, stRegistry, dtReg.getEntityTags(), null, destDir, generatedFiles);
                });
            }
        });
        registry.getDocumentsIds().forEach(id -> {
            if (isCached(id, cachedObjects, sRegistry) && !isAbstract(id, sRegistry)) {
                BuildExceptionUtils.wrapException(() -> {
                    generateJavaCode(id, sRegistry, stRegistry, dtReg.getEntityTags(), "com.gridnine.elsa.common.model.domain.BaseDocument", destDir, generatedFiles);
                });
            }
        });
        registry.getAssetsIds().forEach(id -> {
            if (isCached(id, cachedObjects, sRegistry) && !isAbstract(id, sRegistry)) {
                BuildExceptionUtils.wrapException(() -> {
                    generateJavaCode(id, sRegistry, stRegistry, dtReg.getDatabaseTags(), "com.gridnine.elsa.common.model.domain.BaseAsset", destDir, generatedFiles);
                });
            }
        });
    }

    private<T extends TagDescription>  void generateJavaCode(String id, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, Map<String, T> entityTags, String baseClass, File destDir, Set<File> generatedFiles) throws Exception {
        var ged = JavaCodeGeneratorUtils.buildGenEntityDescription(id, sRegistry,entityTags);
        var eid = sRegistry.getEntities().get(id).getAttributes().get("extends");
        while (eid != null){
            var gen2 = JavaCodeGeneratorUtils.buildGenEntityDescription(eid, sRegistry,entityTags);
            ged.getProperties().putAll(gen2.getProperties());
            eid = sRegistry.getEntities().get(eid).getAttributes().get("extends");
        }
        if(baseClass != null){
            eid = baseClass;
            while (eid != null){
                var gen2 = JavaCodeGeneratorUtils.buildGenEntityDescription(eid, sRegistry,entityTags);
                ged.getProperties().putAll(gen2.getProperties());
                eid = sRegistry.getEntities().get(eid).getAttributes().get("extends");
            }
        }
        generateJavaEntityCode(ged, stRegistry, destDir, generatedFiles);
    }

    private static void generateJavaEntityCode(GenEntityDescription ed, SerializableTypesRegistry sRegistry, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = JavaCodeGeneratorUtils.getPackage(ed.getId());
        gen.addImport("com.gridnine.elsa.common.model.common.Xeption");
        gen.addImport("com.gridnine.elsa.common.model.domain.CachedObject");
        gen.setPackageName(packageName);
        String cnsb = "public class _Cached%s extends %s implements CachedObject".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId()),
                JavaCodeGeneratorUtils.getSimpleName(ed.getId()));
        gen.wrapWithBlock(cnsb, () -> {
            gen.blankLine();
            gen.printLine("private boolean allowChanges = false;");
            for(GenPropertyDescription prop: ed.getProperties().values()){
                var st = sRegistry.getTypes().get(prop.getTagDescription().getType());
                if(st != null && st.isFinalField() && st.getReadonlyJavaQualifiedName() != null){
                    var type = JavaCodeGeneratorUtils.calculateDeclarationType(prop, sRegistry, gen);
                    gen.blankLine();
                    if(!st.getGenerics().isEmpty()){
                        gen.printLine("private final %s %s = new %s<>();".formatted(type, prop.getId(), JavaCodeGeneratorUtils.getSimpleName(st.getReadonlyJavaQualifiedName(), gen)));
                    } else {
                        gen.printLine("private final %s %s = new %s();".formatted(type, prop.getId(), JavaCodeGeneratorUtils.getSimpleName(st.getReadonlyJavaQualifiedName(), gen)));
                    }
                }
            }
            gen.blankLine();
            gen.wrapWithBlock("public void setAllowChanges(boolean allowChanges)", () -> gen.printLine("this.allowChanges = allowChanges;"));

            for(GenPropertyDescription prop: ed.getProperties().values()){
                var st = sRegistry.getTypes().get(prop.getTagDescription().getType());
                var type = JavaCodeGeneratorUtils.calculateDeclarationType(prop, sRegistry, gen);
                if(st != null && st.isFinalField() && st.getReadonlyJavaQualifiedName() != null){
                    gen.blankLine();
                    gen.printLine("@Override");
                    gen.wrapWithBlock("public %s get%s()".formatted(type, BuildTextUtils.capitalize(prop.getId())), () -> {
                        gen.printLine("return this.%s;".formatted(prop.getId()));
                    });
                   continue;
                }
                gen.printLine("@Override");
                gen.wrapWithBlock("public void set%s(%s value)".formatted(BuildTextUtils.capitalize(prop.getId()), type), () -> {
                    gen.wrapWithBlock("if(!allowChanges)", () -> gen.printLine("throw Xeption.forDeveloper(\"changes are not allowed\");"));
                    gen.printLine("super.set%s(value);".formatted(BuildTextUtils.capitalize(prop.getId())));
                });
            }
            gen.printLine("@Override");
            gen.wrapWithBlock("public Object getValue(String propertyName)", () -> {
                for(GenPropertyDescription prop: ed.getProperties().values()){
                    var st = sRegistry.getTypes().get(prop.getTagDescription().getType());
                    if(st != null && st.isFinalField() && st.getReadonlyJavaQualifiedName() != null){
                        gen.blankLine();
                        gen.wrapWithBlock("if(\"%s\".equals(propertyName))".formatted(prop.getId()), () -> gen.printLine("return %s;".formatted(prop.getId())));
                    }
                }
                gen.printLine("return super.getValue(propertyName);");
            });
            gen.printLine("@Override");
            gen.wrapWithBlock("public void setValue(String propertyName, Object value)", () -> {
                gen.wrapWithBlock("if(!allowChanges)", () -> gen.printLine("throw Xeption.forDeveloper(\"changes are not allowed\");"));
                gen.printLine("super.setValue(propertyName, value);");
            });
        });

        var file = JavaCodeGeneratorUtils.saveIfDiffers(gen.toString(), "%s.java"
                .formatted(ed.getId().substring(0, ed.getId().lastIndexOf(".")) + "._Cached" +
                        ed.getId().substring(ed.getId().lastIndexOf(".") + 1)), destDir);
        generatedFiles.add(file);
    }

    private boolean isAbstract(String id, SerializableMetaRegistry sRegistry) {
        return "true".equals(sRegistry.getEntities().get(id).getAttributes().get("extends"));
    }

    private static void collectObjects(String docId, SerializableMetaRegistry totalRegistry, DomainTypesRegistry dtr, Set<String> cachedObjects) {
        if (!cachedObjects.add(docId)) {
            return;
        }
        var ett = totalRegistry.getEntities().get(docId);
        if (ett != null) {
            ett.getProperties().values().forEach(prop -> {
                var tagDescription = dtr.getEntityTags().get(prop.getTagName());
                if("ENTITY".equals(tagDescription.getType())){
                    collectObjects(prop.getAttributes().get(tagDescription.getObjectIdAttributeName()), totalRegistry, dtr, cachedObjects);
                    return;
                }
                processGenerics(tagDescription.getGenerics(), prop, totalRegistry, dtr, cachedObjects);
            });
        }

    }

    private static boolean isCached(String id, Set<String> cachedObjects, SerializableMetaRegistry registry) {
        var iid = id;
        while (iid != null) {
            var doc = registry.getEntities().get(iid);
            if (doc == null) {
                break;
            }
            if (cachedObjects.contains(iid)) {
                return true;
            }
            iid = doc.getAttributes().get("extends");
        }
        return false;
    }

    private static void processGenerics(List<GenericDescription> generics, PropertyDescription prop, SerializableMetaRegistry totalRegistry, DomainTypesRegistry dtr, Set<String> cachedObjects) {
        generics.forEach((gen) ->{
            if("ENTITY".equals(gen.getType())){
                collectObjects(prop.getAttributes().get(gen.getObjectIdAttributeName()), totalRegistry, dtr, cachedObjects);
                return;
            }
            processGenerics(gen.getNestedGenerics(), prop, totalRegistry, dtr, cachedObjects);
        });
    }
}
