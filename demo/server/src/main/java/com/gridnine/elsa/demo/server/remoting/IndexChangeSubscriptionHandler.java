/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.demo.model.remoting.DemoDocumentChangedEvent;
import com.gridnine.elsa.server.remoting.RemotingSubscriptionHandler;

public class IndexChangeSubscriptionHandler implements RemotingSubscriptionHandler<Void, DemoDocumentChangedEvent> {
    @Override
    public boolean isApplicable(DemoDocumentChangedEvent event, Void parameters) throws Exception {
        return true;
    }
}
