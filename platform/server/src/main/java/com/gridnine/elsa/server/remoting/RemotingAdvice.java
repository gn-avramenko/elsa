/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.common.model.common.HasPriority;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;

public interface RemotingAdvice extends HasPriority {
    default void onServerCall(RemotingCallContext context, RunnableWithExceptionAndArgument<RemotingCallContext> callback) throws Exception {
        callback.run(context);
    }

    default void onDownload(RemotingCallContext context, RunnableWithExceptionAndArgument<RemotingCallContext> callback) throws Exception {
        callback.run(context);
    }
}
