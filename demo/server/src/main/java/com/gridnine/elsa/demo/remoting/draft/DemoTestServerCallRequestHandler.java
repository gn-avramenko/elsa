/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.draft;

import com.gridnine.elsa.demo.model.remoting.DemoTestClientCallRequest;
import com.gridnine.elsa.demo.model.remoting.DemoTestClientCallResponse;
import com.gridnine.elsa.demo.model.remoting.DemoTestServerCallRequest;
import com.gridnine.elsa.demo.model.remoting.DemoTestServerCallResponse;
import com.gridnine.elsa.server.core.remoting.RemotingServerCallContext;
import com.gridnine.elsa.server.core.remoting.RemotingServerCallHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DemoTestServerCallRequestHandler implements RemotingServerCallHandler<DemoTestServerCallRequest, DemoTestServerCallResponse> {

    @Autowired
    private DemoRemotingController remotingController;

    @Override
    public String getId() {
        return "demo:test:server-call";
    }

    @Override
    public DemoTestServerCallResponse service(DemoTestServerCallRequest request, RemotingServerCallContext context) throws Exception {
        var param = request.getParam();
        var result = new DemoTestServerCallResponse();
        var clientRequest = new DemoTestClientCallRequest();
        clientRequest.setParam("test");
        DemoTestClientCallResponse clientResponse = remotingController.callClient(context.getClientId(), "test", "client-call", clientRequest);
        result.setStringProperty(clientResponse.getStringProperty());
        result.setDateProperty(LocalDate.now());
        result.setDateTimeProperty(LocalDateTime.now());
        return result;
    }

}
