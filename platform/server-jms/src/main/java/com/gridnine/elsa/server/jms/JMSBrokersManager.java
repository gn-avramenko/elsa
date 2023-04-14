/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

import com.gridnine.elsa.meta.config.Environment;

public interface JMSBrokersManager {
    String getDefaultBrokerId();

    JMSBroker getBroker(String brokerId);
    void registerEmbeddedBroker(final JMSEmbeddedBrokerConfiguration config) throws Exception;

    public void setDefaultBrokerId(final String value);

    public static JMSBrokersManager get(){
        return Environment.getPublished(JMSBrokersManager.class);
    }
}
