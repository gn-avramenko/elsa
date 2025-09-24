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

package com.gridnine.platform.elsa.gradle.codegen.common;


import com.gridnine.platform.elsa.gradle.meta.common.EntityDescription;
import com.gridnine.platform.elsa.gradle.meta.common.EnumDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardPropertyDescription;
import com.gridnine.platform.elsa.gradle.meta.common.StandardValueType;
import com.gridnine.platform.elsa.gradle.meta.remoting.RemotingMetaRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class WebCodeGeneratorUtils {

    public static void generateWebEnumCode(EnumDescription ed, TypeScriptCodeGenerator gen) {
        gen.printLine("export type %s=".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getId())));
        var items = new ArrayList<>(ed.getItems().values());
        for (int n = 0; n < items.size(); n++) {
            if (n == 0) {
                gen.printLine("'%s'".formatted(items.get(n).getId()));
            } else {
                gen.printLine("| '%s'".formatted(items.get(n).getId()));
            }
        }
        gen.print(";\n");
    }

    public static void generateWebEntityCode(EntityDescription ed, RemotingMetaRegistry metaRegistry, TypeScriptCodeGenerator gen) throws Exception {
        var imports = new HashSet<String>();
        for (var pd : ed.getProperties().values()) {
            if(pd.getType() == StandardValueType.ENTITY || pd.getType() == StandardValueType.ENUM){
                imports.add(JavaCodeGeneratorUtils.getSimpleName(pd.getClassName()));
            }
        }
        for (var pd : ed.getCollections().values()) {
            if(pd.getElementType() == StandardValueType.ENTITY|| pd.getElementType() == StandardValueType.ENUM){
                    imports.add(JavaCodeGeneratorUtils.getSimpleName(pd.getElementClassName()));
            }
        }
        for (var pd : ed.getMaps().values()) {
            if(pd.getKeyType() == StandardValueType.ENTITY|| pd.getKeyType() == StandardValueType.ENUM){
                    imports.add(JavaCodeGeneratorUtils.getSimpleName(pd.getKeyClassName()));
            }
            if(pd.getValueType() == StandardValueType.ENTITY|| pd.getValueType() == StandardValueType.ENUM){
                    imports.add(JavaCodeGeneratorUtils.getSimpleName(pd.getValueClassName()));
            }
        }
        if(ed.getExtendsId() != null){
            imports.add(JavaCodeGeneratorUtils.getSimpleName(ed.getExtendsId()));
        }
        if(!imports.isEmpty()){
            if(imports.stream().anyMatch(it -> isAbstract(it, metaRegistry))){
                gen.printLine("import { HasClassName } from 'elsa-web-core'");
            }
            imports.stream().sorted().forEach(it ->{
                if("BinaryData".equals(it)){
                    gen.printLine("import { BinaryData }  from 'elsa-web-core';");
                } else if(!"Object".equals(it)) {
                    gen.printLine("import { %s } from './%s';".formatted(it, it));
                }
            });
            gen.blankLine();
        }

        gen.wrapWithBlock("export type %s=%s".formatted(
                JavaCodeGeneratorUtils.getSimpleName(ed.getId()), ed.getExtendsId()!= null? "%s & ".formatted(JavaCodeGeneratorUtils.getSimpleName(ed.getExtendsId())): ""), () -> {
            for (var pd : ed.getProperties().values()) {
                gen.printLine("%s%s: %s,".formatted(pd.getId(), isNullable(pd) ? "?" : "", getType(pd.getType(), metaRegistry, pd.getClassName())));
            }
            for (var cd : ed.getCollections().values()) {
                gen.printLine("%s: %s[],".formatted(cd.getId(), getType(cd.getElementType(), metaRegistry, cd.getElementClassName())));
            }
            for (var md : ed.getMaps().values()) {
                gen.printLine("%s: Map<%s, %s>,".formatted(md.getId(), getType(md.getKeyType(), metaRegistry, md.getKeyClassName()),
                        getType(md.getValueType(), metaRegistry, md.getValueClassName())));
            }
        });
        gen.print(";\n");
    }

    private static boolean isAbstract(String it, RemotingMetaRegistry metaRegistry) {
        if("Object".equals(it)){
            return true;
        }
        var ett = metaRegistry.getEntities().get(it);
        return ett != null && ett.isAbstract();
    }


    public static String getType(StandardValueType vt, RemotingMetaRegistry metaRegistry, String className) {
        return switch (vt) {
            case LONG, INT, BIG_DECIMAL -> "number";
            case UUID, STRING, CLASS -> "string";
            case LOCAL_DATE, INSTANT, LOCAL_DATE_TIME -> "Date";
            case ENUM -> JavaCodeGeneratorUtils.getSimpleName(className);
            case ENTITY -> {
                if("Object".equals(className)){
                    yield  "HasClassName";
                }
                if(metaRegistry != null && isAbstract(className, metaRegistry)){
                    yield  "%s & HasClassName".formatted(JavaCodeGeneratorUtils.getSimpleName(className));
                }
                yield JavaCodeGeneratorUtils.getSimpleName(className);
            }
            case BOOLEAN -> "boolean";
            case BYTE_ARRAY -> "Uint8Array";
            case ENTITY_REFERENCE -> "EntityReference";
        };
    }

    private static boolean isNullable(StandardPropertyDescription pd) {
        return !pd.isNonNullable();
    }

    public static void generateImportCode(Collection<EntityDescription> values, Set<String> additionalEntities, Map<String, File> tsa, TypeScriptCodeGenerator gen, File file) throws Exception {
        Set<String> entities = new LinkedHashSet<>();
        values.forEach(ett -> {
            ett.getProperties().values().forEach(prop -> {
                if (prop.getType() == StandardValueType.ENTITY || prop.getType() == StandardValueType.ENUM) {
                    entities.add(prop.getClassName());
                }
            });
            ett.getCollections().values().forEach(coll -> {
                if (coll.getElementType() == StandardValueType.ENTITY || coll.getElementType() == StandardValueType.ENUM) {
                    entities.add(coll.getElementClassName());
                }
            });
            ett.getMaps().values().forEach(map -> {
                if (map.getKeyType() == StandardValueType.ENTITY || map.getKeyType() == StandardValueType.ENUM) {
                    entities.add(map.getKeyClassName());
                }
                if (map.getValueType() == StandardValueType.ENTITY || map.getValueType() == StandardValueType.ENUM) {
                    entities.add(map.getValueClassName());
                }
            });
        });
        entities.addAll(additionalEntities);
        var imports = new LinkedHashMap<String, Set<String>>();
        for (String clsName : entities) {
            var sf = tsa.get(clsName);
            if (sf != null && !sf.equals(file)) {
                String relPath;
                if (sf.getParentFile().equals(file.getParentFile())) {
                    relPath = "./%s".formatted(sf.getName());
                } else {
                    relPath = file.getParentFile().toPath().relativize(sf.toPath()).toString();
                }
                relPath = relPath.substring(0, relPath.lastIndexOf('.'));
                imports.computeIfAbsent(relPath, (it) -> new LinkedHashSet<>()).add(JavaCodeGeneratorUtils.getSimpleName(clsName));
            }
        }
        if (imports.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Set<String>> entry : imports.entrySet()) {
            gen.wrapWithBlock("import ", () -> entry.getValue().forEach(value -> gen.printLine("%s,".formatted(value))));
            gen.print(" from '%s';".formatted(entry.getKey()));
        }
        gen.blankLine();

    }

    public static File getFile(String fileName, File destDir){
        var parts = fileName.split("\\.");
        var currentFile = destDir;
        var length = parts.length;
        for (int n = length - 3; n < length - 2; n++) {
            currentFile = new File(currentFile, parts[n] + "/");
            assert currentFile.exists() || currentFile.mkdirs();
        }
        return  new File(currentFile, parts[parts.length - 2] + "." + parts[parts.length - 1]);
    }

    public static File saveIfDiffers(String content, File currentFile) throws IOException {
        while (!currentFile.getParentFile().exists()) {
            try {
                //noinspection BusyWait
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //noops
            }
            //noinspection ResultOfMethodCallIgnored
            currentFile.getParentFile().mkdirs();
        }
        Files.writeString(currentFile.toPath(), content);
        return currentFile;
    }
}
