/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.rest.core.GetServerCallDescriptionRequest;
import com.gridnine.elsa.common.rest.core.GetServerCallDescriptionResponse;
import com.gridnine.elsa.common.rest.core.GetSubscriptionDescriptionRequest;
import com.gridnine.elsa.common.rest.core.GetSubscriptionDescriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class GetSubscriptionDescriptionHandler implements RemotingServerCallHandler<GetSubscriptionDescriptionRequest, GetSubscriptionDescriptionResponse> {

    @Autowired
    private RemotingMetaRegistry remotingMetaRegistry;

    @Override
    public String getId() {
        return "core:meta:get-subscription-description";
    }

    @Override
    public GetSubscriptionDescriptionResponse service(GetSubscriptionDescriptionRequest request, RemotingServerCallContext context) throws Exception {
        var subscriptionDescription = remotingMetaRegistry.getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getSubscriptions().get(request.getSubscriptionId());
        var result = new GetSubscriptionDescriptionResponse();
        result.setParameterClassName(subscriptionDescription.getParameterClassName());
        result.setEventClassName(subscriptionDescription.getEventClassName());
        return result;
    }
}
