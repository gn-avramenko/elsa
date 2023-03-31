/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

public interface RemotingServerCallHandler<RQ,RP> {
    RP service(RQ request, RemotingServerCallContext context) throws Exception;
}
