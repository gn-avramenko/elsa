/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms.activator;

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.jms.JMSBrokersManager;
import com.gridnine.elsa.server.jms.JMSFacade;
import com.gridnine.elsa.server.jms.impl.JMSArtemisBrokersManagerImpl;
import com.gridnine.elsa.server.jms.impl.JMSArtemisFacadeImpl;

public class ServerJmsActivator implements Activator {
    @Override
    public double getOrder() {
        return 6;
    }

    @Override
    public void configure() throws Exception {
        JMSArtemisBrokersManagerImpl manager = new JMSArtemisBrokersManagerImpl();
        Environment.publish(JMSBrokersManager.class, manager);
        Environment.publish(JMSFacade.class, new JMSArtemisFacadeImpl(manager));
    }

    @Override
    public void activate() throws Exception {

    }
}
