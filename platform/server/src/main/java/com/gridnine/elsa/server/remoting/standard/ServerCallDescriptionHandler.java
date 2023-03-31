/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionRequest;
import com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionResponse;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.server.remoting.RemotingServerCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

public class ServerCallDescriptionHandler implements RemotingServerCallHandler<GetServerCallDescriptionRequest, GetServerCallDescriptionResponse> {

    @Override
    public GetServerCallDescriptionResponse service(GetServerCallDescriptionRequest request, RemotingServerCallContext context) throws Exception {
        var result = new GetServerCallDescriptionResponse();
        RemotingServerCallDescription scd = RemotingMetaRegistry.get().getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getServerCalls().get(request.getMethodId());
        result.setRequestClassName(scd.getRequestClassName());
        result.setResponseClassName(scd.getResponseClassName());
        return result;
    }
}
