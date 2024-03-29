/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.model.domain;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;

public abstract class BaseProjection<D extends BaseDocument> extends BaseIntrospectableObject {
    private Integer navigationKey;
    private EntityReference<D> document;

    public static class Fields {
        public static final String navigationKey = "navigationKey";
        public static final String document = "document";
    }

    public Integer getNavigationKey() {
        return navigationKey;
    }

    public void setNavigationKey(Integer navigationKey) {
        this.navigationKey = navigationKey;
    }

    public EntityReference<D> getDocument() {
        return document;
    }

    public void setDocument(EntityReference<D> document) {
        this.document = document;
    }

    @Override
    public Object getValue(String propertyName) {
        if(Fields.navigationKey.equals(propertyName)){
            return navigationKey;
        }
        if(Fields.document.equals(propertyName)){
            return document;
        }
        return super.getValue(propertyName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.navigationKey.equals(propertyName)){
            navigationKey = (Integer) value;
            return;
        }
        if(Fields.document.equals(propertyName)){
            document = (EntityReference<D>) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
