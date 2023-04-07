/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.parser.serializable;

import com.gridnine.elsa.gradle.utils.BuildXmlNode;
import com.gridnine.elsa.gradle.utils.BuildXmlUtils;
import com.gridnine.elsa.meta.serialization.GenericDeclaration;
import com.gridnine.elsa.meta.serialization.GenericsDeclaration;
import com.gridnine.elsa.meta.serialization.SerializableType;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import com.gridnine.elsa.meta.serialization.SingleGenericDeclaration;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class SerializableTypesParser {
    public void updateRegistry(SerializableTypesRegistry registry, File file) throws Exception{
        var node = BuildXmlUtils.parseXml(Files.readAllBytes(file.toPath()));
        for(BuildXmlNode child : node.getChildren("type")){
            var id = child.getAttribute("id");
            var res = new SerializableType();
            res.setId(id);
            registry.getTypes().put(id, res);
            res.setJavaQualifiedName(child.getAttribute("java-qualified-name"));
            res.setReadonlyJavaQualifiedName(child.getAttribute("readonly-java-qualified-name"));
            res.setFinalField("true".equals(child.getAttribute("final-field")));
            res.setTsQualifiedName(child.getAttribute("ts-qualified-name"));
            var generics = child.getFirstChild("generics");
            if(generics != null){
                processGenerics(res.getGenerics(), generics);
            }
            var tsGenerics = child.getFirstChild("ts-generics");
            if(tsGenerics != null){
                processGenerics(res.getTsGenerics(), tsGenerics);
            }
        }
    }

    private void processGenerics(List<GenericDeclaration> generics, BuildXmlNode node) {
        for(BuildXmlNode child: node.getChildren()){
            if("generic".equals(child.getName())){
                var gen = new SingleGenericDeclaration();
                gen.setId(child.getAttribute("id"));
                generics.add(gen);
                continue;
            }
            var gens = new GenericsDeclaration();
            processGenerics(gens.getGenerics(), child);
            generics.add(gens);
        }
    }
}
