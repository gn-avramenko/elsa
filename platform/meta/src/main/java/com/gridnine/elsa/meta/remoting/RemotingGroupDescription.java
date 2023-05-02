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
    private final Map<String, RemotingDownloadDescription> downloads = new LinkedHashMap<>();

    private final Map<String, RemotingUploadDescription> uploads = new LinkedHashMap<>();

    private final Map<String, RemotingSubscriptionDescription> subscriptions = new LinkedHashMap<>();

    public RemotingGroupDescription() {
    }

    public RemotingGroupDescription(String id) {
        super(id);
    }

    public Map<String, RemotingServerCallDescription> getServerCalls() {
        return serverCalls;
    }

    public Map<String, RemotingDownloadDescription> getDownloads() {
        return downloads;
    }

    public Map<String, RemotingUploadDescription> getUploads() {
        return uploads;
    }

    public Map<String, RemotingSubscriptionDescription> getSubscriptions() {
        return subscriptions;
    }
}
