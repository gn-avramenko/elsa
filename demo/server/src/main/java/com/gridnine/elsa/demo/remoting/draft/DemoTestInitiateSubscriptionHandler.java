/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.draft;

import com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionEvent;
import com.gridnine.elsa.server.core.remoting.RemotingServerCallContext;
import com.gridnine.elsa.server.core.remoting.RemotingServerCallHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoTestInitiateSubscriptionHandler implements RemotingServerCallHandler<Void, Void> {


    @Autowired
    private DemoRemotingController demoRemotingController;

    @Override
    public String getId() {
        return "demo:test:initiate-subscription";
    }

    @Override
    public Void service(Void request, RemotingServerCallContext context) throws Exception {
        var event = new DemoTestSubscriptionEvent();
        event.setStringProperty("test\ntest2");
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            demoRemotingController.sendSubscriptionEvent("test", "subscription", event);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.setStringProperty("test3");
            demoRemotingController.sendSubscriptionEvent("test", "subscription", event);
        }).start();
        return null;
    }

}
