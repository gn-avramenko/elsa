/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemotingTypesRegistry {
    private final Map<String, AttributeDescription> remotingAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> groupAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> serverCallAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> downloadAttributes = new LinkedHashMap<>();
    private final Map<String, AttributeDescription> uploadAttributes = new LinkedHashMap<>();

    private final Map<String, AttributeDescription> subscriptionAttributes = new LinkedHashMap<>();

    private final  Map<String, TagDescription> entityTags = new LinkedHashMap<>();

    public Map<String, AttributeDescription> getRemotingAttributes() {
        return remotingAttributes;
    }

    public Map<String, AttributeDescription> getGroupAttributes() {
        return groupAttributes;
    }

    public Map<String, AttributeDescription> getServerCallAttributes() {
        return serverCallAttributes;
    }

    public Map<String, AttributeDescription> getDownloadAttributes() {
        return downloadAttributes;
    }

    public Map<String, TagDescription> getEntityTags() {
        return entityTags;
    }

    public Map<String, AttributeDescription> getUploadAttributes() {
        return uploadAttributes;
    }

    public static RemotingTypesRegistry get(){
        return Environment.getPublished(RemotingTypesRegistry.class);
    }

    public Map<String, AttributeDescription> getSubscriptionAttributes() {
        return subscriptionAttributes;
    }
}
