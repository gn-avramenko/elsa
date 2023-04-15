/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

public interface RemotingDownloadHandler<P> {
    DownloadableResourceWrapper createResource(P request, RemotingCallContext context) throws Exception;
}
