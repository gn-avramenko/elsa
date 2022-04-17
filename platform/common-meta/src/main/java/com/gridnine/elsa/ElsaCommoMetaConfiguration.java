/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa;

import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaCommoMetaConfiguration {
    @Bean
    public CustomMetaRegistry customMetaRegistry(){
        return new CustomMetaRegistry();
    }

    @Bean
    public DomainMetaRegistry domainMetaRegistry(){
        return new DomainMetaRegistry();
    }

}
