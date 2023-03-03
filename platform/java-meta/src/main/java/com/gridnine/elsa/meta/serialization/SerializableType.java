/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import com.gridnine.elsa.meta.common.BaseElementWithId;

public class SerializableType extends BaseElementWithId {
    private String javaQualifiedName;

    public String getJavaQualifiedName() {
        return javaQualifiedName;
    }

    public void setJavaQualifiedName(String javaQualifiedName) {
        this.javaQualifiedName = javaQualifiedName;
    }

}
