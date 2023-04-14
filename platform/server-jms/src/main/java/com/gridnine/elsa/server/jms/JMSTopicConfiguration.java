/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

public class JMSTopicConfiguration extends
        BaseJMSItemConfiguration<JMSTopicConfiguration> {
    private boolean durableSubscribers;

    public boolean isDurableSubscribers() {
        return durableSubscribers;
    }

    public void setDurableSubscribers(final boolean value) {
        durableSubscribers = value;
    }

}
