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

    private String readonlyJavaQualifiedName;

    private boolean finalField;

    private String tsQualifiedName;

    private final List<GenericDeclaration> generics = new ArrayList<>();

    private final List<GenericDeclaration> tsGenerics = new ArrayList<>();

    public String getJavaQualifiedName() {
        return javaQualifiedName;
    }

    public void setJavaQualifiedName(String javaQualifiedName) {
        this.javaQualifiedName = javaQualifiedName;
    }

    public List<GenericDeclaration> getGenerics() {
        return generics;
    }

    public String getReadonlyJavaQualifiedName() {
        return readonlyJavaQualifiedName;
    }

    public void setReadonlyJavaQualifiedName(String readonlyJavaQualifiedName) {
        this.readonlyJavaQualifiedName = readonlyJavaQualifiedName;
    }

    public boolean isFinalField() {
        return finalField;
    }

    public void setFinalField(boolean finalField) {
        this.finalField = finalField;
    }

    public String getTsQualifiedName() {
        return tsQualifiedName;
    }

    public void setTsQualifiedName(String tsQualifiedName) {
        this.tsQualifiedName = tsQualifiedName;
    }

    public List<GenericDeclaration> getTsGenerics() {
        return tsGenerics;
    }
}
