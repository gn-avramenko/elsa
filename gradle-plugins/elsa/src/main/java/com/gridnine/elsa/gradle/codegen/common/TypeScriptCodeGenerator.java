/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.gradle.utils.BuildRunnableWithException;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;

import java.util.HashSet;
import java.util.Set;

public class TypeScriptCodeGenerator {
    private final StringBuffer buf = new StringBuffer();

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
        var sb = new StringBuilder();
        sb.append("""
                /* ****************************************************************
                 * This is generated code, don't modify it manually
                 **************************************************************** */
                 
                 """);
        return sb +buf.toString();
    }
}
