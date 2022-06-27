/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.EnumItemDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingSubscriptionDescription;

public class DemoElsaRemotingMetaRegistryConfigurator implements RemotingMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(RemotingMetaRegistry registry){
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.demo.model.remoting.DemoTestEnum");
			registry.getEnums().put(enumDescription.getId(), enumDescription);
			{
				var enumItemDescription = new EnumItemDescription("ITEM1");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ITEM2");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallRequest");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("param");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallResponse");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("stringProperty");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionParameters");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("param");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionEvent");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("stringProperty");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var groupDescription = new RemotingGroupDescription("test");
			registry.getGroups().put(groupDescription.getId(), groupDescription);
			{
				var serverCallDescription = new RemotingServerCallDescription("test");
				serverCallDescription.setValidatable(false);
				serverCallDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallRequest");
				serverCallDescription.setResponseClassName("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallResponse");
			}
			{
				var subscriptionDescription = new RemotingSubscriptionDescription("test");
				subscriptionDescription.setParameterClassName("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionParameters");
				subscriptionDescription.setEventClassName("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionEvent");
			}
		}
	}
}