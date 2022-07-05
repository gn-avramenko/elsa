/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemotingHandlersRegistry {

    private final Map<String, RemotingServerCallHandler<?,?>> serverCallhandlersMap = new HashMap<>();

    private final Map<String, RemotingSubscriptionHandler<?,?>> subscriptionHandlersMap = new HashMap<>();

    @Autowired(required = false)
    private void setHandlers(List<RemotingServerCallHandler<?,?>> handlers){
        handlers.forEach(h ->{
            serverCallhandlersMap.put(h.getId(), h);
        });
    }

    @Autowired(required = false)
    private void setSubscriptionHandlers(List<RemotingSubscriptionHandler<?,?>> handlers){
        handlers.forEach(h ->{
            subscriptionHandlersMap.put(h.getId(), h);
        });
    }

    public<RQ,RS> RemotingServerCallHandler<RQ,RS> getServerCallHandler(String id){
        return (RemotingServerCallHandler<RQ, RS>) serverCallhandlersMap.get(id);
    }

    public<RP,RE> RemotingSubscriptionHandler<RP,RE> getSubscriptionHandler(String id){
        return (RemotingSubscriptionHandler<RP,RE>) subscriptionHandlersMap.get(id);
    }
}
