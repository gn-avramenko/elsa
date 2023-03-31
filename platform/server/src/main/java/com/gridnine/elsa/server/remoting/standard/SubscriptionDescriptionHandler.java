/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.GetSubscriptionDescriptionRequest;
import com.gridnine.elsa.common.model.remoting.GetSubscriptionDescriptionResponse;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.server.remoting.RemotingServerCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

public class SubscriptionDescriptionHandler implements RemotingServerCallHandler<GetSubscriptionDescriptionRequest, GetSubscriptionDescriptionResponse> {

    @Override
    public GetSubscriptionDescriptionResponse service(GetSubscriptionDescriptionRequest request, RemotingServerCallContext context) throws Exception {
            var result = new GetSubscriptionDescriptionResponse();
            var ssd = RemotingMetaRegistry.get().getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getServerSubscriptions().get(request.getSubscriptionId());
            result.setEventClassName(ssd.getEventClassName());
            result.setParameterClassName(ssd.getParameterClassName());
            return result;
    }
}
