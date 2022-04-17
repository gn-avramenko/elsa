/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public abstract class BaseIdentity extends BaseIntrospectableObject{

    public static class Fields {
        public final static String id = "id";
    }

    public BaseIdentity(){}

    public BaseIdentity(long id){
        this.id = id;
    }

    private long id = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Object getValue(String propertyName) {
        if(Fields.id.equals(propertyName)){
            return id;
        }
        return super.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.id.equals(propertyName)){
            id = (long) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
