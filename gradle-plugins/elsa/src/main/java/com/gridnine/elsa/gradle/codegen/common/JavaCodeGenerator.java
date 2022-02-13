/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.gradle.utils.BuildRunnableWithException;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;

import java.util.HashSet;
import java.util.Set;

public class JavaCodeGenerator {
    private final StringBuffer buf = new StringBuffer();

    private final Set<String> imports = new HashSet<>();

    private String packageName;

    private int indent;

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
        buf.append("\t".repeat(Math.max(0, indent)));
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void blankLine(){
        buf.append("\n");
    }

    public void addImport(String className){
        imports.add(className);
    }

    public String toString(){
        var sb = new StringBuilder();
        sb.append("""
                /*****************************************************************
                 * This is generated code, don't modify it manually
                 *****************************************************************/
               
                """);
        sb.append("package %s;\n\n".formatted(packageName));
        if(!imports.isEmpty()){
            sb.append(BuildTextUtils.joinToString(imports.stream().map("import %s;\n"::formatted).sorted().toList(), ""));
            sb.append("\n");
        }
        return sb +buf.toString();
    }
}
