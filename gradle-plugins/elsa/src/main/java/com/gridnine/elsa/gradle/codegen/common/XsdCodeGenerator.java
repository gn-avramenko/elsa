/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;

import com.gridnine.elsa.gradle.utils.BuildRunnableWithException;

import java.util.LinkedHashMap;
import java.util.Map;

public class XsdCodeGenerator {
    private final StringBuffer buf = new StringBuffer();

    private String targetNamespace;

    public void nameSpace(String value){
        this.targetNamespace = value;
    }

    private int indent=1;

    public void addTag(String tagName, boolean noInternalContent, Map<String,String> attributes){
        indent(buf);
        buf.append("<%s".formatted(tagName));
        for(Map.Entry<String, String> entry: attributes.entrySet()){
           buf.append(" %s=\"%s\"".formatted(entry.getKey(), entry.getValue()));
        }
        if(noInternalContent){
            buf.append("/>\n");
            return;
        }
        buf.append(">\n");
    }


    public Map<String,String> attributes(String... values){
        Map<String,String> attributes = new LinkedHashMap<>();
        for(int n =0; n < values.length; n+=2){
            String name = values[n];
            String value = values[n+1];
            attributes.put(name, value);
        }
        return attributes;
    }

    public void addTag(String name, String... attributes){
        addTag(name, true, attributes(attributes));
    }

    public void wrapWithBlock(String name, Map<String,String> attributes, BuildRunnableWithException runnable) throws Exception {
        addTag(name, false, attributes);
        indent++;
        try{
            runnable.run();
        } finally {
            indent--;
            indent(buf);
            buf.append("</%s>\n".formatted(name));
        }
    }

    private void indent(StringBuffer buf) {
        buf.append("\t".repeat(Math.max(0, indent)));
    }

    public String toString(){
        var sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("""
<schema targetNamespace="%s"
        elementFormDefault="qualified" xmlns="http://www.w3.org/2001/XMLSchema"
        xmlns:tns="%s">
""".formatted(targetNamespace, targetNamespace));
        return sb +buf.toString() + "</schema>";
    }
}
