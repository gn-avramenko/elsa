/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.common.model.common.HasPriority;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;

public interface RemotingAdvice extends HasPriority {
    default void onServerCall(RemotingServerCallContext context, RunnableWithExceptionAndArgument<RemotingServerCallContext> callback) throws Exception {
        callback.run(context);
    }
}
