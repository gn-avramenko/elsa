/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

public class VersionData extends VersionMetadata {

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
}
