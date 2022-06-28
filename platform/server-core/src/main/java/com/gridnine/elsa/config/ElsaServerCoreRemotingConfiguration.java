/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.server.core.remoting.CoreRemotingController;
import com.gridnine.elsa.server.core.remoting.GetRemotingEntityDescriptionHandler;
import com.gridnine.elsa.server.core.remoting.GetServerCallDescriptionHandler;
import com.gridnine.elsa.server.core.remoting.RemotingHandlersRegistry;
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
    public GetRemotingEntityDescriptionHandler getRemotingEntityDescriptionHandler(){
        return new GetRemotingEntityDescriptionHandler();
    }
}
