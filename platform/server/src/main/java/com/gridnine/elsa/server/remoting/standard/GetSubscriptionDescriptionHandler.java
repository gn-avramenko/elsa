/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting.standard;

import com.gridnine.elsa.common.model.remoting.GetSubscriptionDescriptionRequest;
import com.gridnine.elsa.common.model.remoting.GetSubscriptionDescriptionResponse;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingServerCallHandler;

public class GetSubscriptionDescriptionHandler implements RemotingServerCallHandler<GetSubscriptionDescriptionRequest, GetSubscriptionDescriptionResponse> {
    @Override
    public GetSubscriptionDescriptionResponse service(GetSubscriptionDescriptionRequest request, RemotingCallContext context) throws Exception {
        var result = new GetSubscriptionDescriptionResponse();
        var scd = RemotingMetaRegistry.get().getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getSubscriptions().get(request.getSubscriptionId());
        result.setParameterClassName(scd.getParameterClassName());
        result.setEventClassName(scd.getEventClassName());
        return result;
    }
}
