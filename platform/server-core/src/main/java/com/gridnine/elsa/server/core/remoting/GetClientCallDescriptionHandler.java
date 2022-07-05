/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.rest.core.GetClientCallDescriptionRequest;
import com.gridnine.elsa.common.rest.core.GetClientCallDescriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class GetClientCallDescriptionHandler implements RemotingServerCallHandler<GetClientCallDescriptionRequest, GetClientCallDescriptionResponse> {

    @Autowired
    private RemotingMetaRegistry remotingMetaRegistry;

    @Override
    public String getId() {
        return "core:meta:get-client-call-description";
    }

    @Override
    public GetClientCallDescriptionResponse service(GetClientCallDescriptionRequest request, RemotingServerCallContext context) throws Exception {
        var subscriptionDescription = remotingMetaRegistry.getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getClientCalls().get(request.getMethodId());
        var result = new GetClientCallDescriptionResponse();
        result.setRequestClassName(subscriptionDescription.getRequestClassName());
        result.setResponseClassName(subscriptionDescription.getResponseClassName());
        return result;
    }
}
