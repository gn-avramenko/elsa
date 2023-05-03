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


import com.gridnine.elsa.gradle.utils.BuildRunnableWithException;
import kotlin.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeScriptCodeGenerator {
    private final StringBuffer buf = new StringBuffer();

    private final String packageName;

    private final File module;

    private Set<String> javaImports = new HashSet<>();

    private Set<String> tsImports = new HashSet<>();

    private final Map<String, Pair<String, String>> associations;

    private final File projectFolder;

    public TypeScriptCodeGenerator(String packageName, File module, File projectFolder, Map<String, Pair<String, String>> associations) {
        this.packageName = packageName;
        this.module = module;
        this.associations = associations;
        this.projectFolder = projectFolder;
    }

    private int indent;
    public void print(String line){
        indent(buf);
        buf.append(line);
    }
    public void printLine(String line){
        if(buf.length() > 0){
            buf.append("\n");
        }
        indent(buf);
        buf.append(line);
    }

    public void wrapWithBlock(String name, BuildRunnableWithException runnable) throws Exception {
        printLine(name == null?  "{" : "%s{".formatted(name));
        indent++;
        try{
            runnable.run();
        } finally {
            indent--;
            printLine("}");
        }
    }

    private void indent(StringBuffer buf) {
        buf.append("  ".repeat(Math.max(0, indent)));
    }

    public void blankLine(){
        buf.append("\n");
    }

    public String toString(){
        var imports = new HashMap<String, Map<String, List<String>>>();
        var str = projectFolder.toPath().relativize(module.toPath()).toString();
        var thisLocalModuleName = str.substring(0, str.lastIndexOf('.'));
        tsImports.forEach(imp ->{
            var idx = imp.indexOf(":");
            var pack = imp.substring(0, idx);
            var moduleAndClass = imp.substring(idx+1);
            var localModuleName = "";
            var className = moduleAndClass;
            int idx2 = moduleAndClass.lastIndexOf("/");
            if(idx2 != -1){
                localModuleName = !packageName.equals(pack)? "" : moduleAndClass.substring(0, idx2);
                className = moduleAndClass.substring(idx2+1);
            }
            if(packageName.equals(pack) && localModuleName.equals(thisLocalModuleName)){
                return;
            }
            List<String> classes = imports.computeIfAbsent(pack, (p) -> new HashMap<>()).computeIfAbsent(localModuleName, (m) -> new ArrayList<>());
            if(!classes.contains(className)){
                classes.add(className);
            }
        });
        javaImports.forEach(imp ->{
            var association = associations.get(imp);
            if(association != null){
                if(packageName.equals(association.getFirst()) && association.getSecond().equals(thisLocalModuleName)){
                    return;
                }
                var localModuleName = association.getSecond();
                if(!packageName.equals(association.getFirst())){
                    localModuleName = "";
                }
                imports.computeIfAbsent(association.getFirst(), (p) -> new HashMap<>()).computeIfAbsent(localModuleName, (m)-> new ArrayList<>()).add(JavaCodeGeneratorUtils.getSimpleName(imp));
            }
        });
        var ikeys = new ArrayList<>(imports.keySet());
        Collections.sort(ikeys);
        var importBuilder = new StringBuilder();
        for(var pack : ikeys){
            var modules = new ArrayList<>(imports.get(pack).keySet());
            Collections.sort(modules);
            for(var mod: modules){
                var classes = new ArrayList<>(imports.get(pack).get(mod));
                Collections.sort(classes);
                var from = pack;
                if(pack.equals("") || pack.equals(packageName)){
                    var fromModule = new File(projectFolder, mod+".ts");
                    from = module.toPath().relativize(fromModule.toPath()).toString();
                    from = from.substring(0, from.lastIndexOf('.'));
                    if(from.startsWith("../")){
                        from = from.substring(1);
                    }
                }
                importBuilder.append("""
                       import {
                         %s,
                       } from '%s';
                        """.formatted(String.join(",\n  ", classes), from));
            }
        }
        String sb = """
                /* ****************************************************************
                 * This is generated code, don't modify it manually
                 **************************************************************** */
                %s
                """.formatted(importBuilder.toString().stripIndent()).stripIndent();
        return sb + buf;
    }

    public Set<String> getJavaImports() {
        return javaImports;
    }

    public Set<String> getTsImports() {
        return tsImports;
    }
}
