/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.storage;

import com.gridnine.elsa.demo.model.domain.DemoDomainDocument;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection;
import com.gridnine.elsa.server.storage.ProjectionHandler;

import java.util.List;
import java.util.Set;

public class DemoDomainDocumentProjectionHandler implements ProjectionHandler<DemoDomainDocument, DemoDomainDocumentProjection> {
    @Override
    public Class<DemoDomainDocument> getDocumentClass() {
        return DemoDomainDocument.class;
    }

    @Override
    public Class<DemoDomainDocumentProjection> getProjectionClass() {
        return DemoDomainDocumentProjection.class;
    }

    @Override
    public List<DemoDomainDocumentProjection> createProjections(DemoDomainDocument doc, Set<String> properties) throws Exception {
        var proj = new DemoDomainDocumentProjection();
        proj.setEnumProperty(doc.getEnumProperty());
        proj.setGetAllProperty(doc.getGetAllProperty());
        proj.setStringProperty(doc.getStringProperty());
        proj.setEntityReferenceProperty(doc.getEntityReferenceProperty());
        proj.getEnumCollection().addAll(doc.getEnumCollection());
        proj.getEntityRefCollection().addAll(doc.getEntityRefCollection());
        proj.getStringCollection().addAll(doc.getStringCollection());
        proj.setBooleanProperty(doc.getBooleanProperty());
        proj.setBigDecimalProprerty(doc.getBigDecimalProprerty());
        proj.setLongProperty(doc.getLongProperty());
        proj.setDateTimeProperty(doc.getDateTimeProperty());
        proj.setDateProperty(doc.getDateProperty());
        return List.of(proj);
    }
}
