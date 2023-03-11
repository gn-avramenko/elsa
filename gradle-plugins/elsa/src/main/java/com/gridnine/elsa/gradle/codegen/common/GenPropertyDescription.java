/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.common;


import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.common.TagDescription;

public class GenPropertyDescription extends PropertyDescription{
    private TagDescription tagDescription;

    public TagDescription getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(TagDescription tagDescription) {
        this.tagDescription = tagDescription;
    }
}
