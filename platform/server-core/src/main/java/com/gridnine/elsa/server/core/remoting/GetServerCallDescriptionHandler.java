/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.rest.core.GetServerCallDescriptionRequest;
import com.gridnine.elsa.common.rest.core.GetServerCallDescriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class GetServerCallDescriptionHandler implements RemotingServerCallHandler<GetServerCallDescriptionRequest, GetServerCallDescriptionResponse> {

    @Autowired
    private RemotingMetaRegistry remotingMetaRegistry;

    @Override
    public String getId() {
        return "core:meta:get-server-call-description";
    }

    @Override
    public GetServerCallDescriptionResponse service(GetServerCallDescriptionRequest request, RemotingServerCallContext context) throws Exception {
        var serverCall = remotingMetaRegistry.getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getServerCalls().get(request.getMethodId());
        var result = new GetServerCallDescriptionResponse();
        result.setRequestClassName(serverCall.getRequestClassName());
        result.setResponseClassName(serverCall.getResponseClassName());
        return result;
    }
}
