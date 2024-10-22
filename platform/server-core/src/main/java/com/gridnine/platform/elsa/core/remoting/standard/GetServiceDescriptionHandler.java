/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.core.remoting.standard;

import com.gridnine.platform.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.platform.elsa.common.rest.core.GetServiceDescriptionRequest;
import com.gridnine.platform.elsa.common.rest.core.RHttpMethod;
import com.gridnine.platform.elsa.common.rest.core.RServiceDescription;
import com.gridnine.platform.elsa.core.remoting.RemotingCallContext;
import com.gridnine.platform.elsa.core.remoting.RestHandler;
import com.gridnine.platform.elsa.server.core.CoreRemotingConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class GetServiceDescriptionHandler implements RestHandler<GetServiceDescriptionRequest, RServiceDescription> {

    @Autowired
    private RemotingMetaRegistry remotingMetaRegistry;

    @Override
    public String getId() {
        return CoreRemotingConstants.CORE_META_GET_SERVICE_DESCRIPTION;
    }

    @Override
    public RServiceDescription service(GetServiceDescriptionRequest request, RemotingCallContext context) {
        var serviceDescription = remotingMetaRegistry.getRemotings().get(request.getRemotingId()).getGroups().get(request.getGroupId()).getServices().get(request.getServiceId());
        var result = new RServiceDescription();
        result.setRequestClassName(serviceDescription.getRequestClassName());
        result.setResponseClassName(serviceDescription.getResponseClassName());
        result.setPath(serviceDescription.getPath());
        result.setMultipartRequest(serviceDescription.isMultipartRequest());
        result.setMethod(serviceDescription.getMethod() == null? RHttpMethod.POST: RHttpMethod.valueOf(serviceDescription.getMethod().name()));
        return result;
    }
}
