/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: JMSTest
 *
 * $Id$
 *****************************************************************/
package com.gridnine.elsa.server.jms;

public class JMSMessageConfiguration {

	private String topicId;

	private Long delay;

	public String getTopicId() {
		return topicId;
	}

	public Long getDelay() {
		return delay;
	}

	public JMSMessageConfiguration setTopicId(String value) {
		topicId = value;
		return this;
	}

	public JMSMessageConfiguration setDelay(Long value) {
		delay = value;
		return this;
	}

}
