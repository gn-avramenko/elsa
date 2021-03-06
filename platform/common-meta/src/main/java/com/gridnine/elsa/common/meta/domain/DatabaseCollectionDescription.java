/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class DatabaseCollectionDescription extends BaseModelElementDescription {
    private boolean unique;

    private DatabaseCollectionType elementType;

    private String elementClassName;

    private boolean useInTextSearch;

    public DatabaseCollectionDescription() {
    }

    public DatabaseCollectionDescription(String id) {
        super(id);
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public DatabaseCollectionType getElementType() {
        return elementType;
    }

    public void setElementType(DatabaseCollectionType elementType) {
        this.elementType = elementType;
    }

    public String getElementClassName() {
        return elementClassName;
    }

    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }

    public boolean isUseInTextSearch() {
        return useInTextSearch;
    }

    public void setUseInTextSearch(boolean useInTextSearch) {
        this.useInTextSearch = useInTextSearch;
    }
}
