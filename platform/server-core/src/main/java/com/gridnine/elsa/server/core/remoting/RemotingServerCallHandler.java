/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

public interface RemotingServerCallHandler<RQ,RP> {
    String getId();
    RP service(RQ request, RemotingServerCallContext context) throws Exception;
}
