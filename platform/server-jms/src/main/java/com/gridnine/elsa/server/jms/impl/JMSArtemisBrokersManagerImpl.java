/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms.impl;

import com.atomikos.jms.AtomikosConnectionFactoryBean;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.jms.JMSBroker;
import com.gridnine.elsa.server.jms.JMSBrokersManager;
import com.gridnine.elsa.server.jms.JMSEmbeddedBrokerConfiguration;
import com.gridnine.elsa.server.jms.JMSExternalBrokerConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("nls")
public class JMSArtemisBrokersManagerImpl
        implements JMSBrokersManager, Disposable {

    private final Map<String, ServerData> services = new HashMap<>();

    private String defaultBrokerId;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void registerEmbeddedBroker(
            final JMSEmbeddedBrokerConfiguration config) throws Exception {
        String connectionUrl = String.format("vm://%s", config.getId()); //$NON-NLS-1$
        var sc = new ConfigurationImpl();
        sc.addAcceptorConfiguration(config.getId(), connectionUrl);
        sc.setName(config.getId());
        sc.setSecurityEnabled(false);
        sc.setJMXManagementEnabled(config.isUseJMX());
        sc.setPersistenceEnabled(config.isPersistent());
        sc.setMaxDiskUsage(100_000);
        ActiveMQServer server = ActiveMQServers.newActiveMQServer(sc);
        server.start();
        AtomikosConnectionFactoryBean bean = null;
        if(config.isParticipatesInDistributedTransactions()){
            var xacf = new ActiveMQXAConnectionFactory(connectionUrl);
            bean = new AtomikosConnectionFactoryBean();
            bean.setUniqueResourceName("jms-%s".formatted(config.getId()));
            bean.setXaConnectionFactory ( xacf );
            bean.setPoolSize (20);
            bean.init();
        }
        services.put(config.getId(),
            new ServerData(server, connectionUrl, bean));
    }

    public void registerExternalBroker(
            final JMSExternalBrokerConfiguration config)
            throws GeneralSecurityException, IOException, JMSException {
        String connectionUrl =
            String.format("failover:tcp://%s:%s", config.getExternalBrokerIP(), //$NON-NLS-1$
                Integer.toString(config.getExternalBrokerPort()));
        AtomikosConnectionFactoryBean bean = null;
        if(config.isParticipatesInDistributedTransactions()){
            var xacf = new ActiveMQXAConnectionFactory();
            bean = new AtomikosConnectionFactoryBean();
            bean.setUniqueResourceName("jms-%s".formatted(config.getId()));
            bean.setXaConnectionFactory ( xacf );
            bean.setPoolSize (20);
            bean.init();
        }
        services.put(config.getId(),
            new ServerData(null, connectionUrl, bean));
        log.info(String.format("external server registered: id = %s, url = %s", //$NON-NLS-1$
            config.getId(), connectionUrl));
    }

    public void stop() {
        for (ServerData data : services.values()) {
            final ActiveMQServer server = data.service;
            if (null == server) {
                continue;
            }
            try {
                server.stop();
            } catch (Exception e) {
                log.error(String.format("unable to stop server %s", server), //$NON-NLS-1$
                    e);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ActiveMQJMSBrokers: urls = %s",
            services.values().stream().map(ServerData::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public String getDefaultBrokerId() {
        return defaultBrokerId;
    }

    @Override
    public void setDefaultBrokerId(final String value) {
        defaultBrokerId = value;
    }

    @Override
    public JMSBroker getBroker(final String serverId) {
        return services.get(serverId);
    }

    @Override
    public void dispose() {
        stop();
    }


    static class ServerData implements JMSBroker {

        private final ActiveMQServer service;

        private final String connectionUrl;

        private final AtomikosConnectionFactoryBean connectionFactory;

        ServerData(final ActiveMQServer bService, final String url, AtomikosConnectionFactoryBean connectionFactory) {
            service = bService;
            connectionUrl = url;
            this.connectionFactory = connectionFactory;
        }

        @Override
        public String getConnectionUrl() {
            return connectionUrl;
        }

        @Override
        public String toString() {
            return connectionUrl;
        }

        public AtomikosConnectionFactoryBean getConnectionFactory() {
            return connectionFactory;
        }
    }

    public Map<String, ServerData> getServices() {
        return services;
    }
}
