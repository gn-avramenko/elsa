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

    private final Map<String, RemotingServerCallHandler<?,?>> handlersMap = new HashMap<>();
    @Autowired(required = false)
    private void setHandlers(List<RemotingServerCallHandler<?,?>> handlers){
        handlers.forEach(h ->{
            handlersMap.put(h.getId(), h);
        });
    }

    public<RQ,RS> RemotingServerCallHandler<RQ,RS> getHandler(String id){
        return (RemotingServerCallHandler<RQ, RS>) handlersMap.get(id);
    }
}
