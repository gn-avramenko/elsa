/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.common.model.common.HasPriority;
import com.gridnine.elsa.meta.config.Environment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RemotingRegistry {

    private final List<RemotingAdvice> advices = new ArrayList<>();


    public synchronized void register(RemotingAdvice advice){
        advices.add(advice);
        advices.sort(Comparator.comparingDouble(HasPriority::getPriority));
    }

    public List<RemotingAdvice> getAdvices() {
        return advices;
    }

    public static RemotingRegistry get(){
        return Environment.getPublished(RemotingRegistry.class);
    }
}
