/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.common.BaseElement;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemotingGroupDescription extends BaseElement {
    private final Map<String, RemotingServerCallDescription> serverCalls = new LinkedHashMap<>();
    private final Map<String, RemotingServerSubscriptionDescription> serverSubscriptions = new LinkedHashMap<>();

    public RemotingGroupDescription() {
    }

    public RemotingGroupDescription(String id) {
        super(id);
    }

    public Map<String, RemotingServerCallDescription> getServerCalls() {
        return serverCalls;
    }

    public Map<String, RemotingServerSubscriptionDescription> getServerSubscriptions() {
        return serverSubscriptions;
    }
}
