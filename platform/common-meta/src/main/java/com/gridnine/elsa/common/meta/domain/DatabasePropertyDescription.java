/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

public class DatabasePropertyDescription extends BaseModelElementDescription {
    private DatabasePropertyType type;

    private boolean cacheFind;

    private String className;

    private boolean useInTextSearch;

    public DatabasePropertyDescription() {
    }

    public DatabasePropertyDescription(String id) {
        super(id);
    }

    public DatabasePropertyType getType() {
        return type;
    }

    public void setType(DatabasePropertyType type) {
        this.type = type;
    }

    public boolean isCacheFind() {
        return cacheFind;
    }

    public void setCacheFind(boolean cacheFind) {
        this.cacheFind = cacheFind;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isUseInTextSearch() {
        return useInTextSearch;
    }

    public void setUseInTextSearch(boolean useInTextSearch) {
        this.useInTextSearch = useInTextSearch;
    }
}
