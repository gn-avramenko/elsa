/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.common.search.SearchQueryBuilder;
import com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection;
import com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent;
import com.gridnine.elsa.demo.model.remoting.ElsaDemoSubscriptionClient;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.server.storage.Storage;

import java.util.Timer;
import java.util.TimerTask;

public class DemoEventSource implements Disposable {

    private final Timer timer;

    public DemoEventSource(){
        timer = new Timer("demo-event-source");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var event = new DemoDocumentChangedEvent();
                event.setDocument(Storage.get().searchDocuments(DemoDomainDocumentProjection.class, new SearchQueryBuilder().limit(1).build()).get(0).getDocument());
                ElsaDemoSubscriptionClient.test_demo_document_changed_subscription_send_event(event);
            }
        }, 5000, 5000);
    }
    @Override
    public void dispose() throws Throwable {
        timer.cancel();
    }
}
