/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.config;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.StandardCollectionDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.common.meta.custom.CustomMetaRegistryConfigurator;

public class ElsaCommonCoreCustomMetaRegistryConfigurator implements CustomMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(CustomMetaRegistry registry){
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.common.BaseIdentity");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			entityDescription.setAbstract(true);
			{
				var propertyDescription = new StandardPropertyDescription("id");
				propertyDescription.setType(StandardValueType.LONG);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.domain.BaseAsset");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			entityDescription.setAbstract(true);
			entityDescription.setExtendsId("com.gridnine.elsa.common.core.model.common.BaseIdentity");
			{
				var propertyDescription = new StandardPropertyDescription("versionInfo");
				propertyDescription.setType(StandardValueType.ENTITY);
				propertyDescription.setClassName("com.gridnine.elsa.common.core.model.domain.VersionInfo");
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			entityDescription.setAbstract(true);
			{
				var propertyDescription = new StandardPropertyDescription("document");
				propertyDescription.setType(StandardValueType.ENTITY_REFERENCE);
				propertyDescription.setClassName("Object");
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("navigationKey");
				propertyDescription.setType(StandardValueType.INT);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.common.L10nMessage");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("key");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("bundle");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("parameters");
				collectionDescription.setElementType(StandardValueType.ENTITY);
				collectionDescription.setElementClassName("Object");
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.domain.VersionInfo");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("versionNumber");
				propertyDescription.setType(StandardValueType.INT);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("revision");
				propertyDescription.setType(StandardValueType.INT);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("modifiedBy");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("modified");
				propertyDescription.setType(StandardValueType.LOCAL_DATE_TIME);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("comment");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.model.domain.BaseDocument");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			entityDescription.setAbstract(true);
			entityDescription.setExtendsId("com.gridnine.elsa.common.core.model.common.BaseIdentity");
			{
				var propertyDescription = new StandardPropertyDescription("versionInfo");
				propertyDescription.setType(StandardValueType.ENTITY);
				propertyDescription.setClassName("com.gridnine.elsa.common.core.model.domain.VersionInfo");
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
	}
}