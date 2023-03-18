/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository;

import com.gridnine.elsa.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.core.model.domain.BaseDocument;
import com.gridnine.elsa.core.model.domain.BaseProjection;

@SuppressWarnings("unchecked")
public class RepositoryProjectionWrapper<D extends BaseDocument, I extends BaseProjection<D>> extends BaseIntrospectableObject {

    private I projection;
    private String aggregatedData;

    public static class Fields{
        public  static  final String aggregatedData= "aggregatedData";
        public  static  final String projection= "projection";
    }

    public RepositoryProjectionWrapper(I projection, String aggregatedData) {
        this.projection = projection;
        this.aggregatedData = aggregatedData;
    }

    public I getProjection() {
        return projection;
    }

    public void setProjection(I projection) {
        this.projection = projection;
    }

    public String getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(String aggregatedData) {
        this.aggregatedData = aggregatedData;
    }

    @Override
    public Object getValue(String propertyName) {
        if (Fields.aggregatedData.equals(propertyName)) {
            return aggregatedData;
        }
        if (Fields.projection.equals(propertyName)) {
            return projection;
        }
        return projection.getValue(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if (Fields.aggregatedData.equals(propertyName)) {
            aggregatedData = (String) value;
            return;
        }
        if (Fields.projection.equals(propertyName)) {
            projection = (I) value;
            return;
        }
        super.setValue(propertyName, value);
    }
}
