/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.config;

import com.gridnine.elsa.demo.DemoElsaDomainMetaRegistryConfigurator;
import com.gridnine.elsa.demo.DemoElsaRemotingMetaRegistryConfigurator;
import com.gridnine.elsa.demo.activator.ElsaDemoActivator;
import com.gridnine.elsa.demo.remoting.draft.DemoRemotingController;
import com.gridnine.elsa.demo.remoting.draft.DemoTestInitiateSubscriptionHandler;
import com.gridnine.elsa.demo.remoting.draft.DemoTestServerCallRequestHandler;
import com.gridnine.elsa.demo.remoting.draft.DemoTestSubscriptionHandler;
import com.gridnine.elsa.demo.remoting.restws.AuthController;
import com.gridnine.elsa.demo.remoting.restws.RestWsController;
import com.gridnine.elsa.demo.remoting.sse.PrivateRestFluxService;
import com.gridnine.elsa.demo.remoting.sse.PublicRestFluxService;
import com.gridnine.elsa.demo.remoting.sse.PublicRestService;
import com.gridnine.elsa.demo.userAccount.DemoUserAccountProjectionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaDemoServerConfiguration {
    @Bean
    public ElsaDemoActivator demoActivator(){
        return new ElsaDemoActivator();
    }

    @Bean
    public DemoElsaDomainMetaRegistryConfigurator demoElsaDomainMetaRegistryConfigurator(){
        return new DemoElsaDomainMetaRegistryConfigurator();
    }

    @Bean
    public DemoUserAccountProjectionHandler demoUserAccountProjectionHandler(){
        return new DemoUserAccountProjectionHandler();
    }

    @Bean
    public RestWsController restWsController(){
        return new RestWsController();
    }

    @Bean
    public AuthController authController(){
        return new AuthController();
    }

    @Bean
    public PublicRestService publicRestService(){
        return new PublicRestService();
    }
    @Bean
    public PublicRestFluxService public2RestService(){
        return new PublicRestFluxService();
    }

    @Bean
    public PrivateRestFluxService privateRestFluxService(){
        return new PrivateRestFluxService();
    }
    @Bean
    DemoElsaRemotingMetaRegistryConfigurator demoElsaRemotingMetaRegistryConfigurator(){
        return new DemoElsaRemotingMetaRegistryConfigurator();
    }

    @Bean
    DemoTestServerCallRequestHandler demoTestServerCallRequestHandler(){
        return new DemoTestServerCallRequestHandler();
    }

    @Bean
    DemoRemotingController demoRemotingController(){
        return new DemoRemotingController();
    }

    @Bean
    DemoTestSubscriptionHandler demoTestSubscriptionHandler(){
        return new DemoTestSubscriptionHandler();
    }
    @Bean
    DemoTestInitiateSubscriptionHandler demoTestInitiateSubscriptionHandler(){
        return new DemoTestInitiateSubscriptionHandler();
    }
}
