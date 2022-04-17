/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {
    @Bean
    public InvocationCountAdvice invocationCountAdvice(){
        return new InvocationCountAdvice();
    }
}
