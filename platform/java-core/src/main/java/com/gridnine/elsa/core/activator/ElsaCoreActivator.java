/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.activator;

import com.gridnine.elsa.config.ElsaCommonCoreCustomMetaRegistryConfigurator;
import com.gridnine.elsa.core.CoreCustomTypesConfigurator;
import com.gridnine.elsa.core.CoreDomainTypesConfigurator;
import com.gridnine.elsa.core.CoreL10nTypesConfigurator;
import com.gridnine.elsa.core.CoreSerializableTypesConfigurator;
import com.gridnine.elsa.core.config.Activator;
import com.gridnine.elsa.core.l10n.Localizer;
import com.gridnine.elsa.core.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.core.lock.LockManager;
import com.gridnine.elsa.core.lock.standard.StandardLockManager;
import com.gridnine.elsa.core.reflection.ReflectionFactory;
import com.gridnine.elsa.core.serialization.CachedObjectConverter;
import com.gridnine.elsa.core.serialization.Cloner;
import com.gridnine.elsa.core.serialization.JsonMarshaller;
import com.gridnine.elsa.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.core.serialization.SerializationHandlersRegistry;
import com.gridnine.elsa.core.serialization.handlers.*;
import com.gridnine.elsa.core.serialization.metadata.ObjectSerializationMetadataProvider;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;

public class ElsaCoreActivator implements Activator {
    @Override
    public double getOrder() {
        return 0;
    }

    @Override
    public void configure() throws Exception {
        Environment.publish(new SerializableTypesRegistry());
        Environment.publish(new DomainTypesRegistry());
        Environment.publish(new CustomTypesRegistry());
        Environment.publish(new L10nTypesRegistry());
        Environment.publish(new DomainMetaRegistry());
        Environment.publish(new CustomMetaRegistry());
        Environment.publish(new L10nMetaRegistry());
        Environment.publish(new SerializableMetaRegistry());
        new CoreCustomTypesConfigurator().configure();
        new CoreDomainTypesConfigurator().configure();
        new CoreL10nTypesConfigurator().configure();
        new CoreSerializableTypesConfigurator().configure();
        new ElsaCommonCoreCustomMetaRegistryConfigurator().configure();
        Environment.publish(new ReflectionFactory());
        Environment.publish(new ObjectSerializationMetadataProvider());
        Environment.publish(new JsonMarshaller());
        Environment.publish(new JsonUnmarshaller());
        Environment.publish(new Cloner());
        Environment.publish(new CachedObjectConverter());
        Environment.publish(new SerializationHandlersRegistry());
        Environment.publish(LockManager.class, new StandardLockManager());
        Environment.publish(new SupportedLocalesProvider());
        Environment.publish(new Localizer());
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
