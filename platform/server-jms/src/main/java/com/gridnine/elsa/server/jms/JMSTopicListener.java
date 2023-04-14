/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/
package com.gridnine.elsa.server.jms;


public interface JMSTopicListener<T> {

    public void onMessage(T message);

}
