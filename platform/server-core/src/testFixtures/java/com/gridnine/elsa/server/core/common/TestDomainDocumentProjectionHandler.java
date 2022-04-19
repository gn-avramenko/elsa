/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.common;

import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.core.test.model.domain.TestDomainDocumentProjection;
import com.gridnine.elsa.server.core.storage.SearchableProjectionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestDomainDocumentProjectionHandler implements SearchableProjectionHandler<TestDomainDocument, TestDomainDocumentProjection> {
    @Override
    public Class<TestDomainDocument> getDocumentClass() {
        return TestDomainDocument.class;
    }

    @Override
    public Class<TestDomainDocumentProjection> getProjectionClass() {
        return TestDomainDocumentProjection.class;
    }

    @Override
    public List<TestDomainDocumentProjection> createProjections(TestDomainDocument doc, Set<String> props) {
        var proj = new TestDomainDocumentProjection();
        proj.setEnumProperty(doc.getEnumProperty());
        proj.setGetAllProperty(doc.getGetAllProperty());
        proj.setEntityReference(doc.getEntityReference());
        proj.setStringProperty(doc.getStringProperty());
        proj.getEntityRefCollection().addAll(doc.getEntityRefCollection());
        proj.getEnumCollection().addAll(doc.getEnumCollection());
        proj.getStringCollection().addAll(doc.getStringCollection());
        return Collections.singletonList(proj);
    }
}
