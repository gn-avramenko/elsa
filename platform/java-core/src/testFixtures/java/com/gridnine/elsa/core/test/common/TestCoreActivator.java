/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.test.common;

import com.gridnine.elsa.common.core.test.ElsaCommonCoreTestDomainMetaRegistryConfigurator;
import com.gridnine.elsa.core.config.Activator;

public class TestCoreActivator implements Activator {
    @Override
    public double getOrder() {
        return 0.1;
    }

    @Override
    public void configure() throws Exception {
        new ElsaCommonCoreTestDomainMetaRegistryConfigurator().configure();
    }

    @Override
    public void activate() throws Exception {

    }
}
