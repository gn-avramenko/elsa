/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.EnumItemDescription;
import com.gridnine.elsa.common.meta.common.StandardCollectionDescription;
import com.gridnine.elsa.common.meta.common.StandardMapDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.remoting.RemotingClientCallDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingDescription;
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
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestEntity");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("stringProperty");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
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
			{
				var propertyDescription = new StandardPropertyDescription("dateProperty");
				propertyDescription.setType(StandardValueType.LOCAL_DATE);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("dateTimeProperty");
				propertyDescription.setType(StandardValueType.LOCAL_DATE_TIME);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("enumProperty");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.demo.model.remoting.DemoTestEnum");
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("stringCollection");
				collectionDescription.setElementType(StandardValueType.STRING);
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("dateCollection");
				collectionDescription.setElementType(StandardValueType.LOCAL_DATE);
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("entityCollection");
				collectionDescription.setElementType(StandardValueType.ENTITY);
				collectionDescription.setElementClassName("com.gridnine.elsa.demo.model.remoting.DemoTestEntity");
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var mapDescription = new StandardMapDescription("stringMap");
				mapDescription.setKeyType(StandardValueType.STRING);
				mapDescription.setValueType(StandardValueType.STRING);
				entityDescription.getMaps().put(mapDescription.getId(), mapDescription);
			}
			{
				var mapDescription = new StandardMapDescription("dateMap");
				mapDescription.setKeyType(StandardValueType.LOCAL_DATE);
				mapDescription.setValueType(StandardValueType.LOCAL_DATE);
				entityDescription.getMaps().put(mapDescription.getId(), mapDescription);
			}
			{
				var mapDescription = new StandardMapDescription("entityMap");
				mapDescription.setKeyType(StandardValueType.ENTITY);
				mapDescription.setValueType(StandardValueType.ENTITY);
				mapDescription.setKeyClassName("com.gridnine.elsa.demo.model.remoting.DemoTestEntity");
				mapDescription.setValueClassName("com.gridnine.elsa.demo.model.remoting.DemoTestEntity");
				entityDescription.getMaps().put(mapDescription.getId(), mapDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestClientCallRequest");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("param");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.remoting.DemoTestClientCallResponse");
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
			var remotingDescription = new RemotingDescription("demo");
			registry.getRemotings().put(remotingDescription.getId(), remotingDescription);
			{
				var groupDescription = new RemotingGroupDescription("test");
				remotingDescription.getGroups().put(groupDescription.getId(), groupDescription);
				{
					var serverCallDescription = new RemotingServerCallDescription("test");
					serverCallDescription.setValidatable(false);
					serverCallDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.demo.model.remoting.DemoTestServerCallResponse");
					groupDescription.getServerCalls().put("server-call", serverCallDescription);
				}
				{
					var serverCallDescription = new RemotingServerCallDescription("test");
					serverCallDescription.setValidatable(false);
					groupDescription.getServerCalls().put("initiate-subscription", serverCallDescription);
				}
				{
					var clientCallDescription = new RemotingClientCallDescription("client-call");
					clientCallDescription.setRequestClassName("com.gridnine.elsa.demo.model.remoting.DemoTestClientCallRequest");
					clientCallDescription.setResponseClassName("com.gridnine.elsa.demo.model.remoting.DemoTestClientCallResponse");
					groupDescription.getClientCalls().put("client-call", clientCallDescription);
				}
				{
					var subscriptionDescription = new RemotingSubscriptionDescription("subscription");
					subscriptionDescription.setParameterClassName("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionParameters");
					subscriptionDescription.setEventClassName("com.gridnine.elsa.demo.model.remoting.DemoTestSubscriptionEvent");
					groupDescription.getSubscriptions().put("subscription", subscriptionDescription);
				}
			}
		}
	}
}