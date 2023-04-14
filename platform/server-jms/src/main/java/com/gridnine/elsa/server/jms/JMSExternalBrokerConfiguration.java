/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/
package com.gridnine.elsa.server.jms;

public class JMSExternalBrokerConfiguration
        extends BaseJMSBrokerConfiguration<JMSExternalBrokerConfiguration> {

    private int externalBrokerPort;

    private String externalBrokerIP;

    public int getExternalBrokerPort() {
        return externalBrokerPort;
    }

    public String getExternalBrokerIP() {
        return externalBrokerIP;
    }

    public JMSExternalBrokerConfiguration setExternalBrokerPort(
            final int value) {
        externalBrokerPort = value;
        return this;
    }

    public JMSExternalBrokerConfiguration setExternalBrokerIP(
            final String value) {
        externalBrokerIP = value;
        return this;
    }

}
