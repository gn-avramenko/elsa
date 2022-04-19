/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.common;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import com.gridnine.elsa.config.ElsaCommonCoreCustomMetaRegistryConfigurator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ElsaCommonCoreTestConfiguration {

    @Bean
    public DomainMetaRegistryConfigurator elsaCommonCoreTestDomainMetaRegistryConfigurator(){
        return new ElsaCommonCoreTestDomainMetaRegistryConfigurator();
    }
    @Bean
    public ElsaCommonCoreCustomMetaRegistryConfigurator elsaCommonCoreCustomMetaRegistryConfigurator(){
        return new ElsaCommonCoreCustomMetaRegistryConfigurator();
    }
    @Bean
    @ConditionalOnMissingBean(value = ClassMapper.class)
    public ClassMapper classMapper(){
        return new ClassMapper() {
            @Override
            public int getId(String name) {
                throw Xeption.forDeveloper("not implemented");
            }

            @Override
            public String getName(int id) {
                throw Xeption.forDeveloper("not implemented");
            }
        };
    }

}
