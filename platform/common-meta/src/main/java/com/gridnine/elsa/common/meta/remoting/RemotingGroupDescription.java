/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWitId;
import com.gridnine.elsa.common.meta.l10n.L10nMessageDescription;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemotingGroupDescription extends BaseElementWitId {
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
