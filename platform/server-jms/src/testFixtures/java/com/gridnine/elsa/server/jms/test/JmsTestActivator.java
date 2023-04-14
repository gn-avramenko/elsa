/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms.test;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.server.jms.JMSBrokersManager;
import com.gridnine.elsa.server.jms.JMSEmbeddedBrokerConfiguration;

public class JmsTestActivator implements Activator {
    @Override
    public double getOrder() {
        return 6.01;
    }

    @Override
    public void configure() throws Exception {
        //noops
    }

    @Override
    public void activate() throws Exception {
        JMSBrokersManager.get().registerEmbeddedBroker(new JMSEmbeddedBrokerConfiguration().setId("elsa-test")
                .setPesistent(true)
                .setParticipatesInDistributedTransactions(true));
        JMSBrokersManager.get().setDefaultBrokerId("elsa-test");
    }
}
