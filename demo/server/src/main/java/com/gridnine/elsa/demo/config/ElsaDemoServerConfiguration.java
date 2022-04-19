/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.config;

import com.gridnine.elsa.demo.DemoElsaDomainMetaRegistryConfigurator;
import com.gridnine.elsa.demo.activator.ElsaDemoActivator;
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
}
