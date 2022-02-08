/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.domain;

import com.gridnine.elsa.common.meta.common.BaseModelElementDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseSearchableDescription extends BaseModelElementDescription {

    private boolean hidden = false;

    private final Map<String,DatabasePropertyDescription> properties = new LinkedHashMap<>();

    private final Map<String,DatabaseCollectionDescription> collections = new LinkedHashMap<>();

    public Map<String, DatabasePropertyDescription> getProperties() {
        return properties;
    }

    public Map<String, DatabaseCollectionDescription> getCollections() {
        return collections;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
