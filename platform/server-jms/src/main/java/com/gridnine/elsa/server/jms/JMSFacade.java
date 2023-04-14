/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/
package com.gridnine.elsa.server.jms;

import com.gridnine.elsa.meta.config.Environment;

public interface JMSFacade {

    boolean isTopicRegistered(String topicId) throws JMSOperationException;

    void registerTopic(String topicId) throws JMSOperationException;

    void registerTopic(JMSTopicConfiguration config)
            throws JMSOperationException;

    void registerExternalTopic(String topicId) throws JMSOperationException;

    void registerExternalTopic(JMSTopicConfiguration config)
            throws JMSOperationException;

    <T> void registerQueue(String queueId, JMSQueueWorker<T> worker)
            throws JMSOperationException;

    <T> void registerQueue(JMSQueueConfiguration config,
            JMSQueueWorker<T> worker) throws JMSOperationException;

    <T> void publishMessage(T message, JMSMessageConfiguration config)
            throws JMSOperationException;

    <T> void publishMessage(String topicId, T message)
            throws JMSOperationException;

    <T> void registerTopicListener(String topicId, JMSTopicListener<T> listener)
            throws JMSOperationException;

    <T> void registerTopicListener(JMSTopicListener<T> listener,
            JMSTopicListenerConfiguration config) throws JMSOperationException;

    void stopAllQueueWorkers() throws JMSOperationException;

    public static JMSFacade get(){
        return Environment.getPublished(JMSFacade.class);
    }
}
