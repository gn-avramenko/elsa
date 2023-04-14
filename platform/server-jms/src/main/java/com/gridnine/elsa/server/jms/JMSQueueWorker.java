/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/


package com.gridnine.elsa.server.jms;


public interface JMSQueueWorker<T> {

    public boolean processRequest(T message) throws Exception;

}
