/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.activator;

import com.gridnine.elsa.common.CoreCustomTypesConfigurator;
import com.gridnine.elsa.common.CoreDomainTypesConfigurator;
import com.gridnine.elsa.common.CoreL10nTypesConfigurator;
import com.gridnine.elsa.common.CoreRemotingMetaRegistryConfigurator;
import com.gridnine.elsa.common.CoreRemotingTypesConfigurator;
import com.gridnine.elsa.common.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.common.ElsaCommonCustomMetaRegistryConfigurator;
import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.common.l10n.Localizer;
import com.gridnine.elsa.common.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.serialization.CachedObjectConverter;
import com.gridnine.elsa.common.serialization.Cloner;
import com.gridnine.elsa.common.serialization.JsonMarshaller;
import com.gridnine.elsa.common.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.serialization.SerializationHandlersRegistry;
import com.gridnine.elsa.common.serialization.StringUnmarshaller;
import com.gridnine.elsa.common.serialization.handlers.*;
import com.gridnine.elsa.common.serialization.metadata.ObjectSerializationMetadataProvider;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonActivator implements Activator {
    @Override
    public double getOrder() {
        return 0;
    }

    @Override
    public void configure() throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("").setLevel(Level.FINEST);
        Environment.publish(new SerializableTypesRegistry());
        Environment.publish(new DomainTypesRegistry());
        Environment.publish(new CustomTypesRegistry());
        Environment.publish(new L10nTypesRegistry());
        Environment.publish(new DomainMetaRegistry());
        Environment.publish(new CustomMetaRegistry());
        Environment.publish(new L10nMetaRegistry());
        Environment.publish(new RemotingTypesRegistry());
        Environment.publish(new RemotingMetaRegistry());
        Environment.publish(new SerializableMetaRegistry());
        new CoreCustomTypesConfigurator().configure();
        new CoreDomainTypesConfigurator().configure();
        new CoreL10nTypesConfigurator().configure();
        new CoreSerializableTypesConfigurator().configure();
        new ElsaCommonCustomMetaRegistryConfigurator().configure();
        new CoreRemotingTypesConfigurator().configure();
        new CoreRemotingMetaRegistryConfigurator().configure();
        Environment.publish(new ReflectionFactory());
        Environment.publish(new ObjectSerializationMetadataProvider());
        Environment.publish(new JsonMarshaller());
        Environment.publish(new JsonUnmarshaller());
        Environment.publish(new Cloner());
        Environment.publish(new CachedObjectConverter());
        Environment.publish(new SerializationHandlersRegistry());
        Environment.publish(new SupportedLocalesProvider());
        Environment.publish(new Localizer());
        Environment.publish(new StringUnmarshaller());
        SerializationHandlersRegistry.get().register(new StringSerializationHandler());
        SerializationHandlersRegistry.get().register(new ArrayListSerializationHandler());
        SerializationHandlersRegistry.get().register(new BigDecimalSerializationHandler());
        SerializationHandlersRegistry.get().register(new BooleanSerializationHandler());
        SerializationHandlersRegistry.get().register(new EntityReferenceSerializationHandler());
        SerializationHandlersRegistry.get().register(new EntitySerializationHandler());
        SerializationHandlersRegistry.get().register(new EnumSerializationHandler());
        SerializationHandlersRegistry.get().register(new IntSerializationHandler());
        SerializationHandlersRegistry.get().register(new LocalDateSerializationHandler());
        SerializationHandlersRegistry.get().register(new LocalDateTimeSerializationHandler());
        SerializationHandlersRegistry.get().register(new LongSerializationHandler());

    }

    @Override
    public void activate() throws Exception {

    }
}
