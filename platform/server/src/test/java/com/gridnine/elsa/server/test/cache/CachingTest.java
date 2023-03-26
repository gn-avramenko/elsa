/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test.cache;

import com.gridnine.elsa.common.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjection;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjectionFields;
import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.storage.StorageRegistry;
import com.gridnine.elsa.server.test.ServerTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CachingTest extends ServerTestBase {

    private final InvocationCountAdvice invocationCountAdvice = new InvocationCountAdvice();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        StorageRegistry.get().register(invocationCountAdvice);
    }

    @Test
    public void testCache(){
        AuthContext.setCurrentUser("system");
        var storage = Storage.get();
        var doc = new TestDomainDocument();
        doc.setStringProperty("test");
        storage.saveDocument(doc);
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), false);
        Assertions.assertEquals(1, invocationCountAdvice.getLoadDocumentCount());
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), false);
        Assertions.assertEquals(1, invocationCountAdvice.getLoadDocumentCount());
        Assertions.assertTrue(doc instanceof CachedObject);
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), true);
        Assertions.assertEquals(2, invocationCountAdvice.getLoadDocumentCount());
        doc.setStringProperty("test2");
        storage.saveDocument(doc);
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), false);
        Assertions.assertTrue(doc instanceof CachedObject);
        Assertions.assertEquals(3, invocationCountAdvice.getLoadDocumentCount());
        var ref = storage.findUniqueDocumentReference(TestDomainDocumentProjection.class,
                TestDomainDocumentProjectionFields.stringProperty, "test2");
        Assertions.assertNotNull(ref);
        Assertions.assertEquals(1, invocationCountAdvice.getFindDocumentCount());
        ref = storage.findUniqueDocumentReference(TestDomainDocumentProjection.class,
                TestDomainDocumentProjectionFields.stringProperty, "test2");
        Assertions.assertNotNull(ref);
        Assertions.assertEquals(1, invocationCountAdvice.getFindDocumentCount());
        doc = storage.loadDocument(TestDomainDocument.class, doc.getId(), true);
        doc.setStringProperty("test");
        storage.saveDocument(doc);
        ref = storage.findUniqueDocumentReference(TestDomainDocumentProjection.class,
                TestDomainDocumentProjectionFields.stringProperty, "test");
        Assertions.assertNotNull(ref);
        Assertions.assertEquals(2, invocationCountAdvice.getFindDocumentCount());
        storage.deleteDocument(doc);
        ref = storage.findUniqueDocumentReference(TestDomainDocumentProjection.class,
                TestDomainDocumentProjectionFields.stringProperty, "test");
        Assertions.assertNull(ref);
    }

    @Test
    public void testGetAllCache(){
        invocationCountAdvice.setGetAllDocumentsCount(0);
        invocationCountAdvice.setLoadDocumentCount(0);
        Storage storage = Storage.get();
        AuthContext.setCurrentUser("system");
        {
            var doc = new TestDomainDocument();
            doc.setStringProperty("1");
            doc.setGetAllProperty("test");
            storage.saveDocument(doc);
            storage.loadDocument(TestDomainDocument.class, doc.getId(), false);
            Assertions.assertEquals(1, invocationCountAdvice.getLoadDocumentCount());
        }
        {
            var doc = new TestDomainDocument();
            doc.setStringProperty("2");
            doc.setGetAllProperty("test");
            storage.saveDocument(doc);
            storage.loadDocument(TestDomainDocument.class, doc.getId(), false);
            Assertions.assertEquals(2, invocationCountAdvice.getLoadDocumentCount());
        }

        {
            var docRefs = storage.getAllDocumentReferences(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test");
            Assertions.assertEquals(2, docRefs.size());
            Assertions.assertEquals(1, invocationCountAdvice.getGetAllDocumentsCount());
            docRefs = storage.getAllDocumentReferences(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test");
            Assertions.assertEquals(2, docRefs.size());
            Assertions.assertEquals(1, invocationCountAdvice.getGetAllDocumentsCount());
            var docs = storage.getAllDocuments(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test", false);
            Assertions.assertEquals(2, docs.size());
            Assertions.assertEquals(2, invocationCountAdvice.getLoadDocumentCount());
            invocationCountAdvice.setGetAllDocumentsCount(0);
        }

        {
            var doc = storage.getAllDocuments(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test", true).stream()
                    .filter(it -> "2".equals(it.getStringProperty())).findFirst().orElse(null);
            Assertions.assertEquals(0, invocationCountAdvice.getGetAllDocumentsCount());
            doc.setStringProperty("3");
            storage.saveDocument(doc);
            var docRefs = storage.getAllDocumentReferences(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test");
            Assertions.assertEquals(2, docRefs.size());
            Assertions.assertEquals(0, invocationCountAdvice.getGetAllDocumentsCount());
        }
        {
            var docs = storage.getAllDocuments(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test", true);
            var doc = docs.stream()
                    .filter(it -> "1".equals(it.getStringProperty())).findFirst().orElse(null);
            storage.deleteDocument(doc);
            var docRefs = storage.getAllDocumentReferences(TestDomainDocumentProjection.class,
                    TestDomainDocumentProjectionFields.getAllProperty, "test");
            Assertions.assertEquals(1, docRefs.size());
            Assertions.assertEquals(1, invocationCountAdvice.getGetAllDocumentsCount());
        }
    }
}
