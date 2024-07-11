package com.gridnine.platform.elsa.demo.app;

import com.gridnine.platform.elsa.config.ElsaDemoRemotingConstants;
import com.gridnine.platform.elsa.core.remoting.RemotingCallContext;
import com.gridnine.platform.elsa.core.remoting.RestHandler;
import com.gridnine.platform.elsa.demo.TestRequest;
import com.gridnine.platform.elsa.demo.TestResponse;

public class TestRestHandler implements RestHandler<TestRequest, TestResponse> {
    @Override
    public TestResponse service(TestRequest request, RemotingCallContext context) throws Exception {
        var response = new TestResponse();
        Thread.sleep(2000);
        response.setTestData(request.getTestParam());
        return response;
    }

    @Override
    public String getId() {
        return ElsaDemoRemotingConstants.DEMO_MAIN_TEST_REQUEST;
    }
}
