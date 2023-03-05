/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import com.gridnine.elsa.meta.common.BaseElementWithId;

import java.util.ArrayList;
import java.util.List;

public class SerializableType extends BaseElementWithId {
    private String javaQualifiedName;

    private final List<GenericDeclaration> generics = new ArrayList<>();

    public String getJavaQualifiedName() {
        return javaQualifiedName;
    }

    public void setJavaQualifiedName(String javaQualifiedName) {
        this.javaQualifiedName = javaQualifiedName;
    }

    public List<GenericDeclaration> getGenerics() {
        return generics;
    }
}
