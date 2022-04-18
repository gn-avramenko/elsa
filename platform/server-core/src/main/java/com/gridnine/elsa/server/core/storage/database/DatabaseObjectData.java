/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database;

import com.gridnine.elsa.common.core.model.domain.VersionInfo;

public class DatabaseObjectData extends VersionInfo {

    private DatabaseBinaryData data;

    public static class Fields{
        public static final String data="data";
    }

    public DatabaseBinaryData getData() {
        return data;
    }

    public void setData(DatabaseBinaryData data) {
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
            data = (DatabaseBinaryData) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
