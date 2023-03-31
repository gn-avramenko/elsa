/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;

public class StandardRemotingParameters {

    public static ContextParameterId<RemotingDescription> REMOTING_DESCRIPTION = ()->"REMOTING_DESCRIPTION";
    public static ContextParameterId<RemotingServerCallDescription> SERVER_CALL_DESCRIPTION = ()->"SERVER_CALL_DESCRIPTION";


}
