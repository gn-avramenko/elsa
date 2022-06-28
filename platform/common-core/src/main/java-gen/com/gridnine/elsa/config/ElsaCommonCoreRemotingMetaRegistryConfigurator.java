/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.EnumItemDescription;
import com.gridnine.elsa.common.meta.common.StandardCollectionDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.remoting.RemotingDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.remoting.RemotingServerCallDescription;

public class ElsaCommonCoreRemotingMetaRegistryConfigurator implements RemotingMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(RemotingMetaRegistry registry){
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.common.rest.core.RemotingEntityValueType");
			registry.getEnums().put(enumDescription.getId(), enumDescription);
			{
				var enumItemDescription = new EnumItemDescription("STRING");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("LOCAL_DATE");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("LOCAL_DATE_TIME");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ENUM");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("CLASS");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("BOOLEAN");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("BYTE_ARRAY");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ENTITY");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ENTITY_REFERENCE");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("LONG");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("INT");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("BIG_DECIMAL");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.RemotingEntityPropertyDescription");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("type");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.common.rest.core.RemotingEntityValueType");
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("id");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("className");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("isAbsctract");
				propertyDescription.setType(StandardValueType.BOOLEAN);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.RemotingEntityCollectionDescription");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("elementType");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.common.rest.core.RemotingEntityValueType");
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("id");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("elementClassName");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("isAbsctract");
				propertyDescription.setType(StandardValueType.BOOLEAN);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.RemotingEntityMapDescription");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("id");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("keyType");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.common.rest.core.RemotingEntityValueType");
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("keyIsAbsctract");
				propertyDescription.setType(StandardValueType.BOOLEAN);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("keyClassName");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("valueType");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.common.rest.core.RemotingEntityValueType");
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("valueClassName");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("valueIsAbsctract");
				propertyDescription.setType(StandardValueType.BOOLEAN);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.RemotingEntityDescription");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var collectionDescription = new StandardCollectionDescription("properties");
				collectionDescription.setElementType(StandardValueType.ENTITY);
				collectionDescription.setElementClassName("com.gridnine.elsa.common.rest.core.RemotingEntityPropertyDescription");
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("collections");
				collectionDescription.setElementType(StandardValueType.ENTITY);
				collectionDescription.setElementClassName("com.gridnine.elsa.common.rest.core.RemotingEntityCollectionDescription");
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("maps");
				collectionDescription.setElementType(StandardValueType.ENTITY);
				collectionDescription.setElementClassName("com.gridnine.elsa.common.rest.core.RemotingEntityMapDescription");
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.GetServerCallDescriptionRequest");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("remotingId");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("groupId");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("methodId");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.GetServerCallDescriptionResponse");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("requestClassName");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("responseClassName");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.GetRemotingEntityDescriptionRequest");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("entityId");
				propertyDescription.setType(StandardValueType.STRING);
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.rest.core.GetRemotingEntityDescriptionResponse");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("description");
				propertyDescription.setType(StandardValueType.ENTITY);
				propertyDescription.setClassName("com.gridnine.elsa.common.rest.core.RemotingEntityDescription");
				propertyDescription.setNonNullable(true);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var remotingDescription = new RemotingDescription("core");
			registry.getRemotings().put(remotingDescription.getId(), remotingDescription);
			{
				var groupDescription = new RemotingGroupDescription("meta");
				remotingDescription.getGroups().put(groupDescription.getId(), groupDescription);
				{
					var serverCallDescription = new RemotingServerCallDescription("meta");
					serverCallDescription.setValidatable(false);
					serverCallDescription.setRequestClassName("com.gridnine.elsa.common.rest.core.GetServerCallDescriptionRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.common.rest.core.GetServerCallDescriptionResponse");
					groupDescription.getServerCalls().put("get-server-call-description", serverCallDescription);
				}
				{
					var serverCallDescription = new RemotingServerCallDescription("meta");
					serverCallDescription.setValidatable(false);
					serverCallDescription.setRequestClassName("com.gridnine.elsa.common.rest.core.GetRemotingEntityDescriptionRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.common.rest.core.GetRemotingEntityDescriptionResponse");
					groupDescription.getServerCalls().put("get-entity-description", serverCallDescription);
				}
			}
		}
	}
}