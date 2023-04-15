/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import java.io.File;

public interface RemotingBigFileHandler<P> {
    File getFile(P request, RemotingCallContext context) throws Exception;
}
