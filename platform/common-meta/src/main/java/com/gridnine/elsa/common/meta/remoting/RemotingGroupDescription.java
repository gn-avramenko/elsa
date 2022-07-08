/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemotingGroupDescription extends BaseElementWithId {
    private final Map<String, RemotingServerCallDescription> serverCalls = new LinkedHashMap<>();

    private final Map<String, RemotingClientCallDescription> clientCalls = new LinkedHashMap<>();

    private final Map<String, RemotingSubscriptionDescription> subscriptions = new LinkedHashMap<>();

    public RemotingGroupDescription() {
    }

    public RemotingGroupDescription(String id) {
        super(id);
    }

    public Map<String, RemotingServerCallDescription> getServerCalls() {
        return serverCalls;
    }

    public Map<String, RemotingClientCallDescription> getClientCalls() {
        return clientCalls;
    }

    public Map<String, RemotingSubscriptionDescription> getSubscriptions() {
        return subscriptions;
    }
}
