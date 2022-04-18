/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;

public class DatabaseAssetWrapper<A extends BaseAsset> {

    private A asset;
    private String aggregatedData;
    public DatabaseAssetWrapper(){}

    public static class Fields{
        public  static  final String aggregatedData= "aggregatedData";
    }

    public DatabaseAssetWrapper(A asset, String aggregatedData) {
        this.asset = asset;
        this.aggregatedData = aggregatedData;
    }



    public A getAsset() {
        return asset;
    }

    public void setAsset(A asset) {
        this.asset = asset;
    }

    public String getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(String aggregatedData) {
        this.aggregatedData = aggregatedData;
    }
}
