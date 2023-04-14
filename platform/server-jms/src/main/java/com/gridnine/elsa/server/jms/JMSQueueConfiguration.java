/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

public class JMSQueueConfiguration extends
        BaseJMSItemConfiguration<JMSQueueConfiguration> {

    private boolean useAutoAcknowledge;

    private boolean clusterWide;

    public boolean isUseAutoAcknowledge() {
        return useAutoAcknowledge;
    }

    public JMSQueueConfiguration setUseAutoAcknowledge(final boolean value) {
        useAutoAcknowledge = value;
        return this;
    }

    public boolean isClusterWide() {
        return clusterWide;
    }

    public JMSQueueConfiguration setClusterWide(final boolean value) {
        clusterWide = value;
        return this;
    }

}
