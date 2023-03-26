/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.test.common;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.test.ElsaCommonTestDomainMetaRegistryConfigurator;

public class TestCoreActivator implements Activator {
    @Override
    public double getOrder() {
        return 0.1;
    }

    @Override
    public void configure() throws Exception {
        new ElsaCommonTestDomainMetaRegistryConfigurator().configure();
    }

    @Override
    public void activate() throws Exception {

    }
}
