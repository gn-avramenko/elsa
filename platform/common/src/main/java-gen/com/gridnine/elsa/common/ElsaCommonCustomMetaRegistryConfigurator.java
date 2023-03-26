/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common;

import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.custom.CustomMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;

public class ElsaCommonCustomMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var cmr = Environment.getPublished(CustomMetaRegistry.class);
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.common.BaseIdentity");
			entityDescription.getAttributes().put("abstract", "true");
			{
				var propertyDescription = new PropertyDescription("id");
				propertyDescription.setTagName("long-property");
				entityDescription.getProperties().put("id", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.common.BaseIdentity", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.common.BaseIdentity");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.domain.BaseAsset");
			entityDescription.getAttributes().put("abstract", "true");
			entityDescription.getAttributes().put("extends", "com.gridnine.elsa.common.model.common.BaseIdentity");
			{
				var propertyDescription = new PropertyDescription("versionInfo");
				propertyDescription.setTagName("entity-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.model.domain.VersionInfo");
				entityDescription.getProperties().put("versionInfo", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.domain.BaseAsset", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.domain.BaseAsset");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.domain.BaseProjection");
			entityDescription.getAttributes().put("abstract", "true");
			{
				var propertyDescription = new PropertyDescription("document");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.model.domain.BaseDocument");
				entityDescription.getProperties().put("document", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("navigationKey");
				propertyDescription.setTagName("int-property");
				entityDescription.getProperties().put("navigationKey", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.domain.BaseProjection", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.domain.BaseProjection");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.common.L10nMessage");
			{
				var propertyDescription = new PropertyDescription("key");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("key", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("bundle");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("bundle", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("parameters");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "Object");
				entityDescription.getProperties().put("parameters", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.common.L10nMessage", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.common.L10nMessage");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.domain.VersionInfo");
			{
				var propertyDescription = new PropertyDescription("versionNumber");
				propertyDescription.setTagName("int-property");
				entityDescription.getProperties().put("versionNumber", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("revision");
				propertyDescription.setTagName("int-property");
				entityDescription.getProperties().put("revision", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("modifiedBy");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("modifiedBy", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("modified");
				propertyDescription.setTagName("local-date-time-property");
				entityDescription.getProperties().put("modified", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("comment");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("comment", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.domain.VersionInfo", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.domain.VersionInfo");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.model.domain.BaseDocument");
			entityDescription.getAttributes().put("abstract", "true");
			entityDescription.getAttributes().put("extends", "com.gridnine.elsa.common.model.common.BaseIdentity");
			{
				var propertyDescription = new PropertyDescription("versionInfo");
				propertyDescription.setTagName("entity-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.model.domain.VersionInfo");
				entityDescription.getProperties().put("versionInfo", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.model.domain.BaseDocument", entityDescription);
			cmr.getEntitiesIds().add("com.gridnine.elsa.common.model.domain.BaseDocument");
		}
	}
}