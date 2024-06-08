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

package com.gridnine.platform.elsa.config;

import com.gridnine.platform.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.platform.elsa.core.remoting.MetadataBasedOpenApiFactory;
import com.gridnine.platform.elsa.core.remoting.RemotingChannels;
import com.gridnine.platform.elsa.core.remoting.RemotingHandlersRegistry;
import com.gridnine.platform.elsa.core.remoting.RemotingHttpServlet;
import com.gridnine.platform.elsa.core.remoting.standard.GetL10nBundleHandler;
import com.gridnine.platform.elsa.core.remoting.standard.GetRemotingEntityDescriptionHandler;
import com.gridnine.platform.elsa.core.remoting.standard.GetServiceDescriptionHandler;
import com.gridnine.platform.elsa.core.remoting.standard.GetSubscriptionDescriptionHandler;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerCoreRemotingConfiguration {

    private final RemotingMetaRegistry remotingRegistry;

    @Value("${elsa.remoting.apiPrefix:api}")
    private String apiPrefix;


    @Autowired
    public ElsaServerCoreRemotingConfiguration(RemotingMetaRegistry remotingRegistry) {
        this.remotingRegistry = remotingRegistry;
    }

    @Bean
    public RemotingHandlersRegistry remotingHandlersRegistry() {
        return new RemotingHandlersRegistry();
    }


    @Bean
    public GetServiceDescriptionHandler getRestDescriptionHandler() {
        return new GetServiceDescriptionHandler();
    }

    @Bean
    public GetSubscriptionDescriptionHandler getSubscriptionDescriptionHandler() {
        return new GetSubscriptionDescriptionHandler();
    }

    @Bean(name = "OpenAPI")
    public OpenAPI getCustomOpenApi(){
        var api = new MetadataBasedOpenApiFactory(remotingRegistry).createOpenApi();
        api.addServersItem(new Server().url("/"+apiPrefix));
        return api;
    }

    @Bean
    public GetRemotingEntityDescriptionHandler getRemotingEntityDescriptionHandler() {
        return new GetRemotingEntityDescriptionHandler();
    }

    @Bean
    public GetL10nBundleHandler l10nBundleHandler() {
        return new GetL10nBundleHandler();
    }

    @Bean
    public RemotingHttpServlet remotingHttpServlet() {
        return new RemotingHttpServlet();
    }

    @Bean
    public RemotingChannels remotingChannels() {
        return new RemotingChannels();
    }
}
