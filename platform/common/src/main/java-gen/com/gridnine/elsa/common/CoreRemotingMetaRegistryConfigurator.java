/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common;

import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.EnumItemDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

public class CoreRemotingMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var rmr = Environment.getPublished(RemotingMetaRegistry.class);
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.common.model.remoting.REntityType");
			{
				var enumItemDescription = new EnumItemDescription("DOMAIN_ENTITY");
				enumDescription.getItems().put("DOMAIN_ENTITY", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("DOMAIN_DATABASE_ENTITY");
				enumDescription.getItems().put("DOMAIN_DATABASE_ENTITY", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("CUSTOM");
				enumDescription.getItems().put("CUSTOM", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("REMOTING");
				enumDescription.getItems().put("REMOTING", enumItemDescription);
			}
			smr.getEnums().put("com.gridnine.elsa.common.model.remoting.REntityType", enumDescription);
			rmr.getEnumsIds().add("com.gridnine.elsa.common.model.remoting.REntityType");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RGenericDeclaration");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("nestedGenerics");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RGenericDeclaration");
				entityDescription.getProperties().put("nestedGenerics", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RGenericDeclaration", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RGenericDeclaration");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RSerializableType");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("generics");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RGenericDeclaration");
				entityDescription.getProperties().put("generics", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RSerializableType", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RSerializableType");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RGenericDescription");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("type");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("type", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("objectIdAttributeName");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("objectIdAttributeName", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("nestedGenerics");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RGenericDescription");
				entityDescription.getProperties().put("nestedGenerics", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RGenericDescription", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RGenericDescription");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RTagDescription");
			{
				var propertyDescription = new PropertyDescription("tagName");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("tagName", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("type");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("type", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("objectIdAttributeName");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("objectIdAttributeName", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("generics");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RGenericDescription");
				entityDescription.getProperties().put("generics", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RTagDescription", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RTagDescription");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RAttribute");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("value");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("value", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RAttribute", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RAttribute");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RPropertyDescription");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("tagName");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("tagName", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("attributes");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RAttribute");
				entityDescription.getProperties().put("attributes", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RPropertyDescription", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RPropertyDescription");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.REntityDescription");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("properties");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RPropertyDescription");
				entityDescription.getProperties().put("properties", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("attributes");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RAttribute");
				entityDescription.getProperties().put("attributes", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.REntityDescription", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.REntityDescription");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionRequest");
			{
				var propertyDescription = new PropertyDescription("remotingId");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("remotingId", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("groupId");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("groupId", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("methodId");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("methodId", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionRequest", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionRequest");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionResponse");
			{
				var propertyDescription = new PropertyDescription("requestClassName");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("requestClassName", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("responseClassName");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("responseClassName", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionResponse", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionResponse");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.RTypesMetadata");
			{
				var propertyDescription = new PropertyDescription("serializableTypes");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RSerializableType");
				entityDescription.getProperties().put("serializableTypes", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("domainEntityTags");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RTagDescription");
				entityDescription.getProperties().put("domainEntityTags", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("domainDatabaseTags");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RTagDescription");
				entityDescription.getProperties().put("domainDatabaseTags", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("customEntityTags");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RTagDescription");
				entityDescription.getProperties().put("customEntityTags", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("l10nParameterTypeTags");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RTagDescription");
				entityDescription.getProperties().put("l10nParameterTypeTags", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("remotingEntityTags");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.model.remoting.RTagDescription");
				entityDescription.getProperties().put("remotingEntityTags", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.RTypesMetadata", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.RTypesMetadata");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionRequest");
			{
				var propertyDescription = new PropertyDescription("entityId");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("entityId", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionRequest", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionRequest");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionResponse");
			{
				var propertyDescription = new PropertyDescription("type");
				propertyDescription.setTagName("enum-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.model.remoting.REntityType");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("type", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("description");
				propertyDescription.setTagName("entity-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.model.remoting.REntityDescription");
				propertyDescription.getAttributes().put("non-nullable", "true");
				entityDescription.getProperties().put("description", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionResponse", entityDescription);
			rmr.getEntitiesIds().add("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionResponse");
		}
		{
			var remotingDescription = new RemotingDescription("core");
			remotingDescription.getAttributes().put("xmlns", "http://gridnine.com/elsa/meta-remoting");
			rmr.getRemotings().put("core", remotingDescription);
			{
				var groupDescription = new RemotingGroupDescription("meta");
				remotingDescription.getGroups().put("meta", groupDescription);
				{
					var serverCallDescription = new RemotingServerCallDescription("get-server-call-description");
					serverCallDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.server.remoting.standard.ServerCallDescriptionHandler");
					serverCallDescription.setRequestClassName("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.common.model.remoting.GetServerCallDescriptionResponse");
					groupDescription.getServerCalls().put("get-server-call-description", serverCallDescription);
				}
				{
					var serverCallDescription = new RemotingServerCallDescription("get-types-metadata");
					serverCallDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.server.remoting.standard.TypesMetadataRemotingHandler");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.common.model.remoting.RTypesMetadata");
					groupDescription.getServerCalls().put("get-types-metadata", serverCallDescription);
				}
				{
					var serverCallDescription = new RemotingServerCallDescription("get-entity-description");
					serverCallDescription.getAttributes().put("handler-class-name", "com.gridnine.elsa.server.remoting.standard.EntityDescriptionRemotingHandler");
					serverCallDescription.setRequestClassName("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionRequest");
					serverCallDescription.setResponseClassName("com.gridnine.elsa.common.model.remoting.GetRemotingEntityDescriptionResponse");
					groupDescription.getServerCalls().put("get-entity-description", serverCallDescription);
				}
			}
		}
	}
}