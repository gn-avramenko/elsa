/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingDownloadDescription;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.remoting.RemotingUploadDescription;

public class StandardRemotingParameters {

    public static ContextParameterId<RemotingDescription> REMOTING_DESCRIPTION = ()->"REMOTING_DESCRIPTION";
    public static ContextParameterId<RemotingServerCallDescription> SERVER_CALL_DESCRIPTION = ()->"SERVER_CALL_DESCRIPTION";

    public static ContextParameterId<RemotingDownloadDescription> DOWNLOAD_DESCRIPTION = ()->"DOWNLOAD_DESCRIPTION";
    public static ContextParameterId<RemotingUploadDescription> UPLOAD_DESCRIPTION = ()->"UPLOAD_DESCRIPTION";



}
