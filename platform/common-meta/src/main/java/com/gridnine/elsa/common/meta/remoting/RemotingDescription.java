/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.meta.remoting;

import com.gridnine.elsa.common.meta.common.BaseElementWithId;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemotingDescription extends BaseElementWithId {
    private final Map<String, RemotingGroupDescription> groups = new LinkedHashMap<>();

    public RemotingDescription() {
    }

    public RemotingDescription(String id) {
        super(id);
    }

    public Map<String, RemotingGroupDescription> getGroups() {
        return groups;
    }
}
