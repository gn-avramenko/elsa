/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.common;

import java.util.ArrayList;
import java.util.List;

public class GenericDescription {
    private String id;
    private String type;
    private String objectIdAttributeName;
    private final List<GenericDescription> nestedGenerics = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectIdAttributeName() {
        return objectIdAttributeName;
    }

    public void setObjectIdAttributeName(String objectIdAttributeName) {
        this.objectIdAttributeName = objectIdAttributeName;
    }

    public List<GenericDescription> getNestedGenerics() {
        return nestedGenerics;
    }
}
