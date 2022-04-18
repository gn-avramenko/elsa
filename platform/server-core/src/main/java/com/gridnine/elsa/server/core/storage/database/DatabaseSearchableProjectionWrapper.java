/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database;

import com.gridnine.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.core.model.domain.BaseDocument;
import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.domain.SearchableProjectionDescription;

import java.util.Collection;

@SuppressWarnings("unchecked")
public class DatabaseSearchableProjectionWrapper<D extends BaseDocument, I extends BaseSearchableProjection<D>> extends BaseIntrospectableObject {

    private I projection;
    private String aggregatedData;
    private final SearchableProjectionDescription descr;

    public static class Fields{
        public  static  final String aggregatedData= "aggregatedData";
    }

    public DatabaseSearchableProjectionWrapper(DomainMetaRegistry reg, Class<I> projectionCls) {
        this.descr = reg.getSearchableProjections().get(projectionCls.getName());
    }

    public DatabaseSearchableProjectionWrapper(I projection, DomainMetaRegistry reg, String aggregatedData) {
        this.projection = projection;
        this.aggregatedData = aggregatedData;
        this.descr = reg.getSearchableProjections().get(projection.getClass().getName());
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
        if (BaseSearchableProjection.Fields.navigationKey.equals(propertyName)) {
            return projection.getNavigationKey();
        }
        if (BaseSearchableProjection.Fields.document.equals(propertyName)) {
            return projection.getDocument();
        }
        if (descr.getProperties().containsKey(propertyName)) {
            return projection.getValue(propertyName);
        }
        return projection.getCollection(propertyName);
    }

    @Override
    public void setValue(String propertyName, Object value) {
        if (Fields.aggregatedData.equals(propertyName)) {
            aggregatedData = (String) value;
            return;
        }
        if (BaseSearchableProjection.Fields.navigationKey.equals(propertyName)) {
            projection.setNavigationKey((Integer) value);
            return;
        }
        if (BaseSearchableProjection.Fields.document.equals(propertyName)) {
            projection.setDocument((EntityReference<D>) value);
            return;
        }

        if (descr.getProperties().containsKey(propertyName)) {
            projection.setValue(propertyName, value);
            return;
        }
        var coll = (Collection<Object>) projection.getCollection(propertyName);
        coll.clear();
        coll.addAll((Collection<Object>) value);
    }
}
