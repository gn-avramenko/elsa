/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.boot;

import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import org.springframework.stereotype.Component;

@Component
public class TestDomainMetadataProvider2 implements DomainMetaRegistryConfigurator {
    @Override
    public void updateMetaRegistry(DomainMetaRegistry registry) {
        System.out.println("applied provider 2");
    }
}
