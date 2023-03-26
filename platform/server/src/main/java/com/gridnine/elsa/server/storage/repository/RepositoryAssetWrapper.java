/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository;


import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.domain.BaseAsset;

public class RepositoryAssetWrapper<A extends BaseAsset> extends BaseIntrospectableObject {

    private A asset;
    private String aggregatedData;
    public RepositoryAssetWrapper(){}

    public static class Fields{
        public  static  final String aggregatedData= "aggregatedData";
        public  static  final String asset= "asset";
    }

    public RepositoryAssetWrapper(A asset, String aggregatedData) {
        this.asset = asset;
        this.aggregatedData = aggregatedData;
    }

    @Override
    public Object getValue(String propertyName) {
        if(Fields.asset.equals(propertyName)){
            return this.asset;
        }
        if(Fields.aggregatedData.equals(propertyName)){
            return this.aggregatedData;
        }
        return super.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if(Fields.asset.equals(propertyName)){
            this.asset = (A) value;
            return;
        }
        if(Fields.aggregatedData.equals(propertyName)){
            this.aggregatedData = (String) value;
            return;
        }
        super.setValue(propertyName, value);
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
