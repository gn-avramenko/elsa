/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository;

import com.gridnine.elsa.core.model.domain.VersionInfo;

public class RepositoryObjectData extends VersionInfo {

    private RepositoryBinaryData data;

    public static class Fields{
        public static final String data="data";
    }

    public RepositoryBinaryData getData() {
        return data;
    }

    public void setData(RepositoryBinaryData data) {
        this.data = data;
    }

    public void setRevision(int revision){
        setValue(VersionInfo.Fields.revision, revision);
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
            data = (RepositoryBinaryData) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
