/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;

import java.io.File;
import java.util.*;

public class WebCodeGeneratorUtils {

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

    public static void generateWebEntityCode(EntityDescription ed,  TypeScriptCodeGenerator gen) throws Exception {
        gen.wrapWithBlock("export type %s=".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId())), () ->{
            for(var pd: ed.getProperties().values()){
                gen.printLine("%s%s: %s,".formatted(pd.getId(), isNullable(pd)? "?": "", getType(pd.getType(), pd.getClassName())));
            }
            for(var cd: ed.getCollections().values()){
                gen.printLine("%s: %s[],".formatted(cd.getId(),  getType(cd.getElementType(), cd.getElementClassName())));
            }
            for(var md: ed.getMaps().values()){
                gen.printLine("%s: Map<%s, %s>,".formatted(md.getId(),  getType(md.getKeyType(), md.getKeyClassName()),
                        getType(md.getValueType(), md.getValueClassName())));
            }
        });
        gen.print(";\n");
    }

    private static String getType(StandardValueType vt, String className) {
        return switch (vt){
            case LONG,INT,BIG_DECIMAL -> "number";
            case STRING,CLASS -> "string";
            case LOCAL_DATE, LOCAL_DATE_TIME -> "Date";
            case ENTITY,  ENUM -> JavaCodeGeneratorUtils.getSimpleName(className);
            case BOOLEAN -> "boolean";
            case BYTE_ARRAY -> "Uint8Array";
            case ENTITY_REFERENCE -> "EntityReference";
        };
    }

    private static boolean isNullable(StandardPropertyDescription pd) {
        return !pd.isNonNullable();
    }

    public static void generateImportCode(Collection<EntityDescription> values, Set<String> additionalEntities, Map<String, File> tsa, TypeScriptCodeGenerator gen, File file) throws Exception{
        Set<String> entities = new LinkedHashSet<>();
        values.forEach(ett -> {
            ett.getProperties().values().forEach(prop ->{
                if(prop.getType() == StandardValueType.ENTITY || prop.getType() == StandardValueType.ENUM ){
                    entities.add(prop.getClassName());
                }
            });
            ett.getCollections().values().forEach(coll ->{
                if(coll.getElementType() == StandardValueType.ENTITY || coll.getElementType() == StandardValueType.ENUM ){
                    entities.add(coll.getElementClassName());
                }
            });
            ett.getMaps().values().forEach(map ->{
                if(map.getKeyType() == StandardValueType.ENTITY || map.getKeyType() == StandardValueType.ENUM ){
                    entities.add(map.getKeyClassName());
                }
                if(map.getValueType() == StandardValueType.ENTITY || map.getValueType() == StandardValueType.ENUM ){
                    entities.add(map.getValueClassName());
                }
            });
        });
        entities.addAll(additionalEntities);
        var imports = new LinkedHashMap<String, Set<String>>();
        for(String clsName: entities){
            var sf = tsa.get(clsName);
            if(sf != null && !sf.equals(file)){
                String relPath;
                if(sf.getParentFile().equals(file.getParentFile())){
                    relPath = "./%s".formatted(sf.getName());
                } else {
                    relPath = file.getParentFile().toPath().relativize(sf.toPath()).toString();
                }
                relPath = relPath.substring(0, relPath.length()-3);
                imports.computeIfAbsent(relPath, (it) -> new LinkedHashSet<>()).add(JavaCodeGeneratorUtils.getSimpleName(clsName));
            }
        }
        if(imports.isEmpty()){
            return;
        }
        for(Map.Entry<String, Set<String>> entry : imports.entrySet()){
            gen.wrapWithBlock("import ", () ->{
                entry.getValue().forEach(value ->{
                    gen.printLine("%s,".formatted(value));
                });
            });
            gen.print(" from '%s';".formatted(entry.getKey()));
        }
        gen.blankLine();

    }
}
