/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms.impl;

import com.gridnine.elsa.common.config.Configuration;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.server.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class JMSArtemisFacadeImpl implements JMSFacade, Disposable {

    final Map<String, TopicData> topics = new ConcurrentHashMap<>();

    final Map<String, QueueData> queues = new ConcurrentHashMap<>();

    final Logger log = LoggerFactory.getLogger(getClass());

    final JMSArtemisBrokersManagerImpl manager;

    public JMSArtemisFacadeImpl(final JMSArtemisBrokersManagerImpl brokerManager) {
        manager = brokerManager;
    }

    @Override
    public void registerTopic(final String topicId)
            throws JMSOperationException {
        registerTopic(new JMSTopicConfiguration()
            .setBrokerId(manager.getDefaultBrokerId()).setId(topicId));
    }

    @Override
    public void registerExternalTopic(final String topicId)
            throws JMSOperationException {
        registerExternalTopic(new JMSTopicConfiguration()
            .setBrokerId(manager.getDefaultBrokerId()).setId(topicId));
    }

    @Override
    public void registerTopic(final JMSTopicConfiguration config)
            throws JMSOperationException {
        String topicId = config.getId();
        if (topics.get(topicId) != null) {
            throw new IllegalArgumentException(
                String.format("topic %s is already published", topicId)); //$NON-NLS-1$
        }
        TopicConsumerData consumer = createConsumer(config);
        try {
            TopicProducerData producer = createProducer(config);
            topics.put(config.getId(), new TopicData(producer, consumer));
        } catch (Throwable e) {
            try {
                consumer.stop();
            } catch (Exception e1) {
                log.error("unable to stop consumer", e1); //$NON-NLS-1$
            }
            throw new JMSOperationException("unable to create consumer", e); //$NON-NLS-1$
        }

    }

    @Override
    public void registerExternalTopic(final JMSTopicConfiguration config)
            throws JMSOperationException {
        String topicId = config.getId();
        if (topics.get(topicId) != null) {
            throw new IllegalArgumentException(
                String.format("topic %s is already published", topicId)); //$NON-NLS-1$
        }
        final TopicProducerData producer = createProducer(config);
        topics.put(config.getId(), new TopicData(producer, null));
    }

    private TopicProducerData createProducer(
            final JMSTopicConfiguration config) {
        final JMSBroker broker = manager.getBroker(
            config.getBrokerId() == null ? manager.getDefaultBrokerId()
                : config.getBrokerId());
        final String subscriberAddress = broker.getConnectionUrl();
        if(config.isParticipatesInDistributedTransactions()){
            return new TopicProducerData(null, null, null, config);
        }
        var connectionFactory = new ActiveMQConnectionFactory(subscriberAddress);
        // Create a Connection
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            // Create a Session
            Session session =
                connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(config.getId());

            // Create a MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            if (config.isPersistent()) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                if (config.getTimeToLive() != 0) {
                    producer.setTimeToLive(config.getTimeToLive());
                }
            }
            log.info(String.format(
                "producer for topic with id %s successfully registered", //$NON-NLS-1$
                config.getId()));
            return new TopicProducerData(connection, session, producer, config);
        } catch (Exception e) {
            log.error(
                String.format("unable to publish producer for topic with id %s", //$NON-NLS-1$
                    config.getId()),
                e);
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e1) {
                    log.error("unable to close connection", e1); //$NON-NLS-1$
                }
            }
            throw new JMSOperationException(
                "unable to create producer for topic with id %s", e); //$NON-NLS-1$
        }
    }

    private TopicConsumerData createConsumer(
            final JMSTopicConfiguration config) {
        final JMSBroker broker = manager.getBroker(
            config.getBrokerId() == null ? manager.getDefaultBrokerId()
                : config.getBrokerId());
        final String subscriberAddress = broker.getConnectionUrl();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(subscriberAddress);
        final List<TopicListenerData<?>> listeners = new ArrayList<>();
        // Create a Connection
        try {
            Connection connection = connectionFactory.createConnection();
            if (config.isDurableSubscribers()) {
                connection.setClientID(Configuration.get().getValue("app-id", "default"));
            }
            try {
                connection.start();
                Session session;
                session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic destination = session.createTopic(config.getId());
                MessageConsumer consumer = config.isDurableSubscribers()
                    ? session.createDurableSubscriber(destination,
                        config.getId())
                    : session.createConsumer(destination);
                connection.setExceptionListener(new ExceptionListener() {

                    @Override
                    public void onException(final JMSException arg0) {
                        log.error("exception occured", arg0); //$NON-NLS-1$

                    }
                });
                consumer.setMessageListener(new MessageListener() {

                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    @Override
                    public void onMessage(final Message message) {
                        try {
                            Object object = null;
                            if (message instanceof ObjectMessage) {
                                ObjectMessage om = (ObjectMessage) message;
                                object = om.getObject();
                            } else if (message instanceof MapMessage) {
                                MapMessage om = (MapMessage) message;
                                Map<String, Object> result =
                                    new LinkedHashMap<>();
                                for (Enumeration<?> e = om.getMapNames(); e
                                    .hasMoreElements();) {
                                    String name = (String) e.nextElement();
                                    result.put(name, om.getObject(name));
                                }
                                object = result;
                            }
                            log.debug(String.format("map message %s recieved", //$NON-NLS-1$
                                object));
                            if (object != null) {
                                for (TopicListenerData item : listeners) {
                                    item.listener.onMessage(object);
                                }
                            }
                        } catch (Throwable e) {
                            log.error("unable to extract message from topic", //$NON-NLS-1$
                                e);
                        }

                    }
                });

                log.info(String.format(
                    "listener successfully registered for topic %s, adress %s", //$NON-NLS-1$
                    config.getId(), subscriberAddress));
                return new TopicConsumerData(connection, session, consumer,
                    config, listeners);
            } catch (Exception e) {
                log.error(
                    "unable to register listener for topic " + config.getId(), //$NON-NLS-1$
                    e);
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e1) {
                        log.error("unable to close connection", e1); //$NON-NLS-1$
                    }
                }
                throw new JMSOperationException("unable to create topic " //$NON-NLS-1$
                    + config.getId(), e);
            }
        } catch (Exception e) {
            throw new JMSOperationException("unable to create topic " //$NON-NLS-1$
                + config.getId(), e);
        }

    }

    @Override
    public <T> void registerTopicListener(final String topicId,
            final JMSTopicListener<T> listener) throws JMSOperationException {
        registerTopicListener(listener,
            new JMSTopicListenerConfiguration().setTopicId(topicId));

    }

    @Override
    public <T> void registerTopicListener(final JMSTopicListener<T> listener,
            final JMSTopicListenerConfiguration config)
            throws JMSOperationException {
        String topicId = config.getTopicId();
        TopicData topicData = topics.get(topicId);
        if (topicData == null) {
            throw new IllegalArgumentException(
                String.format("topic %s is not published", topicId)); //$NON-NLS-1$
        }
        if (null == topicData.consumerData) {
            throw new IllegalArgumentException(String
                .format("write only topic %s not allowed listeners", topicId)); //$NON-NLS-1$
        }
        topicData.consumerData.listeners
            .add(new TopicListenerData<>(listener, config));

    }

    @Override
    public <T> void publishMessage(final T message,
            final JMSMessageConfiguration config) {
        if (message instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) message;
            for (Object key : map.keySet()) {
                if (!(key instanceof String)) {
                    throw new IllegalArgumentException(String.format(
                        "keys in message %s map must be String", message)); //$NON-NLS-1$
                }
            }
        } else if (!(message instanceof Serializable)) {
            throw new IllegalArgumentException(String.format(
                "message %s must be either map or serializable object", //$NON-NLS-1$
                message));
        }

        if((topics.get(config.getTopicId()) != null  && topics.get(config.getTopicId()).producerData.config.isParticipatesInDistributedTransactions())
        || (queues.get(config.getTopicId()) != null  && queues.get(config.getTopicId()).producerData.config.isParticipatesInDistributedTransactions())){
            sendXAMessage(message, config);
            return;
        }
        BaseItemData<?, ?, ?> data = topics.get(config.getTopicId());
        if (data == null) {
            data = queues.get(config.getTopicId());
        }
        if (data == null) {
            throw new IllegalArgumentException(String.format(
                    "neither topic nor queue with id %s is not published", //$NON-NLS-1$
                    config.getTopicId()));
        }
        try {
            if (message instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) message;
                MapMessage mapMessage =
                        data.producerData.session.createMapMessage();
                for (Entry<String, Object> entry : map.entrySet()) {
                    mapMessage.setObject(entry.getKey(),
                            entry.getValue());
                }
                data.producerData.producer.send(mapMessage);
            } else {
                ObjectMessage objectMessage = data.producerData.session.createObjectMessage((Serializable) message);
                data.producerData.producer.send(objectMessage);
            }
            log.debug(String.format("message %s successfully sent", //$NON-NLS-1$
                    message));
        } catch (Throwable e) {
            log.error(
                    String.format("unable to send message %s", message), e); //$NON-NLS-1$
            throw new JMSOperationException("unable to send message", //$NON-NLS-1$
                    e);
        }
    }

    private <T> void sendXAMessage(T message, JMSMessageConfiguration config) {
        ExceptionUtils.wrapException(()->{
            String brokerId = null;
            boolean persistent = false;
            boolean queue = false;
            if(topics.get(config.getTopicId()) != null){
                brokerId = topics.get(config.getTopicId()).producerData.config.getBrokerId();
                persistent = topics.get(config.getTopicId()).producerData.config.isPersistent();
            } else {
                brokerId = queues.get(config.getTopicId()).producerData.config.getBrokerId();
                persistent = queues.get(config.getTopicId()).producerData.config.isPersistent();
                queue = true;
            }
            var cf = manager.getServices().get(brokerId).getConnectionFactory();
            try(var connection = cf.createConnection()) {
                connection.start();
                try {
                    Session session =
                            connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                    Destination destination = queue ? session.createQueue(config.getTopicId()) : session.createTopic(config.getTopicId());

                    MessageProducer producer = session.createProducer(destination);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    if (persistent) {
                        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                    }
                    if (message instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> map = (Map<String, Object>) message;
                        MapMessage mapMessage = session.createMapMessage();
                        for (Entry<String, Object> entry : map.entrySet()) {
                            mapMessage.setObject(entry.getKey(),
                                    entry.getValue());
                        }
                        producer.send(mapMessage);
                    } else {
                        ObjectMessage objectMessage = session.createObjectMessage((Serializable) message);
                        producer.send(objectMessage);
                    }

                } finally {
                    connection.stop();
                }
            }
        });
    }

    @Override
    public <T> void publishMessage(final String topicId, final T message) {
        publishMessage(message,
            new JMSMessageConfiguration().setTopicId(topicId));
    }

    public void stop() {
        stopAllQueueWorkers();
        stop(topics.values());
        stop(queues.values());
    }

    private <T extends BaseJMSItemConfiguration<T>, P extends BaseProducerData<T>, C extends BaseConsumerData<T>, F extends BaseItemData<T, P, C>> void stop(
            final Collection<F> values) {
        for (F topic : values) {
            try {
                try {
                    topic.producerData.stop();
                } finally {
                    if (topic.consumerData != null
                        && !topic.isConsumerStopped()) {
                        topic.consumerData.stop();
                    }
                }
            } catch (Exception e) {
                log.error("unable to stop consumers or producer of topic " //$NON-NLS-1$
                    + topic, e);
            }
        }
    }

    static class BaseProducerData<T extends BaseJMSItemConfiguration<T>> {

        final Logger log = LoggerFactory.getLogger(getClass());

        final MessageProducer producer;

        final Session session;

        final Connection connection;

        final T config;

        BaseProducerData(final Connection cnn, final Session ss,
                final MessageProducer prod, final T conf) {
            connection = cnn;
            session = ss;
            producer = prod;
            config = conf;
        }

        void stop() throws Exception {
            log.debug("stopping producer " + config.getId()); //$NON-NLS-1$
            try {
                try {
                    if(producer != null){
                        producer.close();
                    }
                } finally {
                    if(session != null) {
                        session.close();
                    }
                }
            } finally {
                if(connection != null) {
                    connection.close();
                }
            }
            log.debug("producer stopped " + config.getId()); //$NON-NLS-1$
        }

        @Override
        public String toString() {
            return String.format("{broker: %s, topic: %s}", //$NON-NLS-1$
                config.getBrokerId(), config.getId());
        }
    }

    static class TopicProducerData
            extends BaseProducerData<JMSTopicConfiguration> {

        TopicProducerData(final Connection cnn, final Session ss,
                final MessageProducer prod, final JMSTopicConfiguration conf) {
            super(cnn, ss, prod, conf);
        }
    }

    static class QueueProducerData
            extends BaseProducerData<JMSQueueConfiguration> {

        QueueProducerData(final Connection cnn, final Session ss,
                final MessageProducer prod, final JMSQueueConfiguration conf) {
            super(cnn, ss, prod, conf);
        }
    }

    static class BaseConsumerData<T extends BaseJMSItemConfiguration<T>> {

        final Logger log = LoggerFactory.getLogger(getClass());

        final MessageConsumer consumer;

        final Session session;

        final Connection connection;

        final T config;

        BaseConsumerData(final Connection cnn, final Session ss,
                final MessageConsumer prod, final T conf) {
            connection = cnn;
            session = ss;
            consumer = prod;
            config = conf;
        }

        void stop() throws Exception {
            log.debug("stopping consumer " + config.getId()); //$NON-NLS-1$
            try {
                try {
                    consumer.close();
                } finally {
                    session.close();
                }
            } finally {
                connection.close();
            }
            log.debug("stopping consumer stopped " + config.getId()); //$NON-NLS-1$
        }

    }

    static class TopicConsumerData
            extends BaseConsumerData<JMSTopicConfiguration> {

        final List<TopicListenerData<?>> listeners;

        TopicConsumerData(final Connection cnn, final Session ss,
                final MessageConsumer prod, final JMSTopicConfiguration conf,
                final List<TopicListenerData<?>> lsts) {
            super(cnn, ss, prod, conf);
            listeners = lsts;
        }

    }

    static class QueueConsumerData
            extends BaseConsumerData<JMSQueueConfiguration> {

        private final JMSQueueWorker<?> worker;

        private volatile boolean messageBeingProcessed;

        QueueConsumerData(final Connection cnn, final Session ss,
                final MessageConsumer prod, final JMSQueueConfiguration conf,
                final JMSQueueWorker<?> work) {
            super(cnn, ss, prod, conf);
            worker = work;
        }

        JMSQueueWorker<?> getWorker() {
            return worker;
        }

        boolean isMessageBeingProcessed() {
            return messageBeingProcessed;
        }

        void setMessageBeingProcessed(final boolean value) {
            messageBeingProcessed = value;
        }

    }

    static class BaseItemData<T extends BaseJMSItemConfiguration<T>, P extends BaseProducerData<T>, C extends BaseConsumerData<T>> {
        final P producerData;

        final C consumerData;

        private boolean consumerStopped;

        boolean isConsumerStopped() {
            return consumerStopped;
        }

        void setConsumerStopped(final boolean value) {
            consumerStopped = value;
        }

        BaseItemData(final P producer, final C consumer) {
            producerData = producer;
            consumerData = consumer;
        }

    }

    static class TopicData extends
            BaseItemData<JMSTopicConfiguration, TopicProducerData, TopicConsumerData> {

        TopicData(final TopicProducerData producer,
                final TopicConsumerData consumer) {
            super(producer, consumer);
        }

    }

    static class QueueData extends
            BaseItemData<JMSQueueConfiguration, QueueProducerData, QueueConsumerData> {

        QueueData(final QueueProducerData producer,
                final QueueConsumerData consumer) {
            super(producer, consumer);
        }
    }

    static class TopicListenerData<T> {
        final JMSTopicListener<T> listener;

        final JMSTopicListenerConfiguration config;

        public TopicListenerData(final JMSTopicListener<T> lst,
                final JMSTopicListenerConfiguration conf) {
            listener = lst;
            config = conf;
        }
    }

    static class QueueWorkerData<T> {
        final JMSQueueWorker<T> worker;

        final JMSQueueConfiguration config;

        public QueueWorkerData(final JMSQueueWorker<T> lst,
                final JMSQueueConfiguration conf) {
            worker = lst;
            config = conf;
        }
    }

    @Override
    public void dispose() {
        stop();
    }

    @Override
    public String toString() {
        return String.format("ActiveMQJMS: topics: %s, queues: %s", //$NON-NLS-1$
            topics.keySet(), queues.keySet());
    }

    @Override
    public <T> void registerQueue(final JMSQueueConfiguration config,
            final JMSQueueWorker<T> worker) throws JMSOperationException {
        String topicId = config.getId();
        if (queues.get(topicId) != null) {
            throw new IllegalArgumentException(
                String.format("queue %s is already published", topicId)); //$NON-NLS-1$
        }
        QueueConsumerData consumer = createConsumer(config, worker);
        try {
            QueueProducerData producer = createProducer(config);
            queues.put(config.getId(), new QueueData(producer, consumer));
        } catch (Throwable e) {
            try {
                consumer.stop();
            } catch (Exception e1) {
                log.error("unable to stop consumer", e1); //$NON-NLS-1$
            }
            throw new JMSOperationException("unable to create consumer", e); //$NON-NLS-1$
        }

    }

    private <T> QueueConsumerData createConsumer(
            final JMSQueueConfiguration config,
            final JMSQueueWorker<T> worker) {
        final JMSBroker broker = manager.getBroker(
            config.getBrokerId() == null ? manager.getDefaultBrokerId()
                : config.getBrokerId());
        final String subscriberAddress = broker.getConnectionUrl();
        var connectionFactory = new ActiveMQConnectionFactory(subscriberAddress);
        // Create a Connection
        try {
            Connection connection = connectionFactory.createConnection();
            try {
                connection.start();
                Session session;
                session =
                    connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                Destination destination =
                    session.createQueue(config.isClusterWide() ? config.getId()
                        : String.format("%s-%s", //$NON-NLS-1$
                            Configuration.get().getValue("app-id", "default"), config.getId()));
                MessageConsumer consumer = session.createConsumer(destination);
                final QueueConsumerData consumerData = new QueueConsumerData(
                    connection, session, consumer, config, worker);
                consumer.setMessageListener(new MessageListener() {

                    @SuppressWarnings({ "unchecked" })
                    @Override
                    public void onMessage(final Message message) {
                        boolean acknowledged = false;
                        try {
                            consumerData.setMessageBeingProcessed(true);
                            acknowledged = worker.processRequest(
                                (T) ((ObjectMessage) message).getObject());
                        } catch (Throwable e) {
                            log.error(
                                "unable to extract process item from queue " //$NON-NLS-1$
                                    + config.getId(),
                                e);
                        } finally {
                            if (acknowledged || config.isUseAutoAcknowledge()) {
                                try {
                                    message.acknowledge();
                                } catch (Throwable e) {
                                    log.error(
                                        "unable to ucknowledge process item from queue " //$NON-NLS-1$
                                            + config.getId(),
                                        e);
                                }
                            }
                            consumerData.setMessageBeingProcessed(false);
                        }
                    }
                });
                log.info(String.format(
                    "consumer successfully registered for queue %s, adress %s", //$NON-NLS-1$
                    config.getId(), subscriberAddress));
                return consumerData;
            } catch (Exception e) {
                log.error(
                    "unable to register consumer for queue " + config.getId(), //$NON-NLS-1$
                    e);
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException e1) {
                        log.error("unable to close connection", e1); //$NON-NLS-1$
                    }
                }
                throw new JMSOperationException(
                    "unable to create consumer for queue " //$NON-NLS-1$
                        + config.getId(),
                    e);
            }
        } catch (Exception e) {
            throw new JMSOperationException("unable to consumer for queue " //$NON-NLS-1$
                + config.getId(), e);
        }

    }

    @Override
    public <T> void registerQueue(final String queueId,
            final JMSQueueWorker<T> worker) throws JMSOperationException {
        registerQueue(
            new JMSQueueConfiguration().setId(queueId).setPersistent(true),
            worker);
    }

    private QueueProducerData createProducer(
            final JMSQueueConfiguration config) {
        final JMSBroker broker = manager.getBroker(
            config.getBrokerId() == null ? manager.getDefaultBrokerId()
                : config.getBrokerId());
        final String subscriberAddress = broker.getConnectionUrl();

        var connectionFactory = new ActiveMQConnectionFactory(subscriberAddress);
        // Create a Connection
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            // Create a Session
            Session session =
                connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination =
                session.createQueue(config.isClusterWide() ? config.getId()
                    : String.format("%s-%s", Configuration.get().getValue("app-id", "default"), //$NON-NLS-1$
                        config.getId()));

            // Create a MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            if (config.isPersistent()) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                if (config.getTimeToLive() != 0) {
                    producer.setTimeToLive(config.getTimeToLive());
                }
            }
            log.info(String.format(
                "producer for queue with id %s successfully registered", //$NON-NLS-1$
                config.getId()));
            return new QueueProducerData(connection, session, producer, config);
        } catch (Exception e) {
            log.error(
                String.format("unable to publish producer for queue with id %s", //$NON-NLS-1$
                    config.getId()),
                e);
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e1) {
                    log.error("unable to close connection", e1); //$NON-NLS-1$
                }
            }
            throw new JMSOperationException(
                "unable to create producer for queue with id %s", e); //$NON-NLS-1$
        }
    }

    @Override
    public void stopAllQueueWorkers() throws JMSOperationException {
        JMSException t = null;
        for (Entry<String, QueueData> entry : queues.entrySet()) {
            QueueData item = entry.getValue();
            if (null == item.consumerData) {
                continue;
            }
            try {
                item.consumerData.consumer.setMessageListener(null);
            } catch (JMSException e) {
                log.error("unable to to cleanup message listener for " //$NON-NLS-1$
                    + entry.getKey(), e);
                t = e;
            }
        }
        for (QueueData item : queues.values()) {
            if (null == item.consumerData) {
                continue;
            }
            while (item.consumerData.isMessageBeingProcessed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // noops
                }
            }
        }
        if (t != null) {
            throw new JMSOperationException("unable to stop workers", t); //$NON-NLS-1$
        }

    }

    @Override
    public boolean isTopicRegistered(final String topicId)
            throws JMSOperationException {
        return this.topics.containsKey(topicId);
    }

}
