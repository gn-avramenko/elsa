/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/
package com.gridnine.elsa.server.jms;

public class JMSEmbeddedBrokerConfiguration extends
        BaseJMSInternalBrokerConfiguration<JMSEmbeddedBrokerConfiguration> {
	private boolean persistent;

	private boolean useJMX;

	public boolean isPersistent() {
		return persistent;
	}

	public JMSEmbeddedBrokerConfiguration setPesistent(boolean value){
		this.persistent = value;
		return this;
	}

	public boolean isUseJMX() {
		return useJMX;
	}

}
