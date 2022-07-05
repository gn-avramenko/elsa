/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.server.core.remoting.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaServerCoreRemotingConfiguration {

    @Bean
    public RemotingHandlersRegistry remotingHandlersRegistry(){
        return new RemotingHandlersRegistry();
    }

    @Bean
    public CoreRemotingController coreRemotingController(){
        return new CoreRemotingController();
    }

    @Bean
    public GetServerCallDescriptionHandler getRestDescriptionHandler(){
        return new GetServerCallDescriptionHandler();
    }

    @Bean
    public GetSubscriptionDescriptionHandler getSubscriptionDescriptionHandler(){
        return new GetSubscriptionDescriptionHandler();
    }
    @Bean
    public GetRemotingEntityDescriptionHandler getRemotingEntityDescriptionHandler(){
        return new GetRemotingEntityDescriptionHandler();
    }

    @Bean
    public GetClientCallDescriptionHandler getClientCallDescriptionHandler(){
        return new GetClientCallDescriptionHandler();
    }

    @Bean
    StalledChannelsCleaner stalledChannelsCleaner(){
        return new StalledChannelsCleaner();
    }
}
