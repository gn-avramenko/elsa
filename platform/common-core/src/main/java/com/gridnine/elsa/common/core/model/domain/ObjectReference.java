/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.domain;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;

public class ObjectReference<T extends BaseIdentity> extends BaseIdentity{

    public static class Fields{
        public final static String type ="type";
        public final static String caption ="caption";
    }
    private Class<T> type;

    private String caption;

    public ObjectReference() {}

    public ObjectReference(long id, Class<T> type, String caption) {
        super(id);
        this.type = type;
        this.caption = caption;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public Object getValue(String propertyName) {
        if(Fields.type.equals(propertyName)){
            return type;
        }
        if(Fields.caption.equals(propertyName)){
            return caption;
        }
        return super.getValue(propertyName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.type.equals(propertyName)){
            type = (Class<T>) value;
            return;
        }
        if(Fields.caption.equals(propertyName)){
            caption = (String) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
