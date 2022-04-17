/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.l10n.L10nMetaRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaCommonMetaConfiguration {
    @Bean
    public CustomMetaRegistry customMetaRegistry(){
        return new CustomMetaRegistry();
    }

    @Bean
    public DomainMetaRegistry domainMetaRegistry(){
        return new DomainMetaRegistry();
    }

    @Bean
    public L10nMetaRegistry l10nMetaRegistry(){
        return new L10nMetaRegistry();
    }

}
