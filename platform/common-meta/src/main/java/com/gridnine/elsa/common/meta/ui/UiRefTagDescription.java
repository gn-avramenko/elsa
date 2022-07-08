/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.ui;

public class UiRefTagDescription {
    private String tagName;
    private String ref;

    public UiRefTagDescription() {
    }

    public UiRefTagDescription(String tagName, String ref) {
        this.tagName = tagName;
        this.ref = ref;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
