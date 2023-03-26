/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.remoting;

import com.gridnine.elsa.meta.config.Environment;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class RemotingMetaRegistry {
    private final Set<String> enumsIds = new LinkedHashSet<>();

    private final Set<String> entitiesIds = new LinkedHashSet<>();

    private final Map<String, RemotingDescription> remotings = new LinkedHashMap<>();

    public Set<String> getEnumsIds() {
        return enumsIds;
    }

    public Set<String> getEntitiesIds() {
        return entitiesIds;
    }

    public Map<String, RemotingDescription> getRemotings() {
        return remotings;
    }

    public static RemotingMetaRegistry get(){
        return Environment.getPublished(RemotingMetaRegistry.class);
    }
}
