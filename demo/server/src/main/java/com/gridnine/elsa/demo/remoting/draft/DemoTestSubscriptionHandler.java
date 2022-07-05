/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.draft;

import com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionEvent;
import com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionParameters;
import com.gridnine.elsa.server.core.remoting.RemotingSubscriptionHandler;

public class DemoTestSubscriptionHandler implements RemotingSubscriptionHandler<DemoTestSubscriptionParameters, DemoTestSubscriptionEvent> {


    @Override
    public String getId() {
        return "demo:test:subscription";
    }

    @Override
    public boolean isApplicable(DemoTestSubscriptionEvent event, DemoTestSubscriptionParameters parameters) throws Exception {
        return true;
    }


}
