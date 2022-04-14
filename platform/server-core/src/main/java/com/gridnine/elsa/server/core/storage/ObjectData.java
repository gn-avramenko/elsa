/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.VersionInfo;

public class ObjectData extends VersionInfo {

    private BlobWrapper data;


    public static class Fields{
        public static final String data="data";
    }

    public BlobWrapper getData() {
        return data;
    }

    public void setData(BlobWrapper data) {
        this.data = data;
    }

    public void setRevision(int revision){
        setValue(Properties.revision, revision);
    }
    @Override
    public Object getValue(String propertyName) {
        if(Fields.data.equals(propertyName)){
            return data;
        }
        return super.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.data.equals(propertyName)){
            data = (BlobWrapper) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
