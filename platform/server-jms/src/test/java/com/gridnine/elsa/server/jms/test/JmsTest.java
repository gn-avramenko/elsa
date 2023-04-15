/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms.test;

import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjection;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.jms.JMSBrokersManager;
import com.gridnine.elsa.server.jms.JMSFacade;
import com.gridnine.elsa.server.jms.JMSTopicConfiguration;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

public class JmsTest extends JmsTestBase{

    @Test
    public void testNonXAMessage() throws InterruptedException {
        var topicId = "test-topic";
        JMSFacade.get().registerTopic(new JMSTopicConfiguration().setId(topicId).setPersistent(true)
                .setBrokerId(JMSBrokersManager.get().getDefaultBrokerId()));
        var str = new AtomicReference<String>();
        JMSFacade.get().registerTopicListener(topicId, str::set);
        JMSFacade.get().publishMessage(topicId, "test");
        Thread.sleep(1000L);
        Assertions.assertEquals("test", str.get());
    }

    @Test
    public void testTransaction() throws InterruptedException {
        AuthContext.setCurrentUser("system");
        var topicId = "test-topic";
        JMSFacade.get().registerTopic(new JMSTopicConfiguration().setId(topicId).setPersistent(true)
                .setBrokerId(JMSBrokersManager.get().getDefaultBrokerId()).setParticipatesInDistributedTransactions(true));
        var str = new AtomicReference<String>();
        JMSFacade.get().registerTopicListener(topicId, str::set);

        var docsSize = Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size();

        try {
            TransactionManager.get().withTransaction((tc) -> {
                var doc = new TestDomainDocument();
                doc.setStringProperty("test");
                Storage.get().saveDocument(doc, "version1");
                JMSFacade.get().publishMessage(topicId, "test");
                throw new Exception("test");
            });
        } catch (Throwable e) {
            Thread.sleep(1000L);
            Assertions.assertEquals(docsSize, Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size());
            Assertions.assertNull(str.get());
        }
        TransactionManager.get().withTransaction((tc) -> {
            var doc = new TestDomainDocument();
            doc.setStringProperty("test");
            Storage.get().saveDocument(doc, "version1");
            JMSFacade.get().publishMessage(topicId, "test");
        });
        Assertions.assertEquals(docsSize + 1, Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size());
        Assertions.assertEquals("test", str.get());
    }

}
