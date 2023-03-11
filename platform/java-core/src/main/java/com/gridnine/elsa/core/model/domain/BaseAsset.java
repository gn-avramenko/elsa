/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.model.domain;

import com.gridnine.elsa.core.model.common.BaseIdentity;

public abstract class BaseAsset extends BaseIdentity {
    public static class Fields {
        public static final String versionInfo = "versionInfo";
    }
    private VersionInfo versionInfo;

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    @Override
    public Object getValue(String propertyName) {
        if(Fields.versionInfo.equals(propertyName)){
            return versionInfo;
        }
        return super.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.versionInfo.equals(propertyName)){
            versionInfo = (VersionInfo) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
