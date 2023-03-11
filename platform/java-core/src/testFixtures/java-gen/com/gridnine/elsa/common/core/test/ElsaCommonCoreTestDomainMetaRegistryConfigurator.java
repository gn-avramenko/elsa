/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test;

import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.EnumItemDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import java.util.Locale;

public class ElsaCommonCoreTestDomainMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var dmr = Environment.getPublished(DomainMetaRegistry.class);
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.common.core.test.model.domain.TestEnum");
			{
				var enumItemDescription = new EnumItemDescription("ITEM1");
				enumItemDescription.getDisplayNames().put(new Locale("ru"), "Item 1");
				enumDescription.getItems().put("ITEM1", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ITEM2");
				enumItemDescription.getDisplayNames().put(new Locale("ru"), "Item 2");
				enumDescription.getItems().put("ITEM2", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ITEM3");
				enumItemDescription.getDisplayNames().put(new Locale("ru"), "Item 3");
				enumDescription.getItems().put("ITEM3", enumItemDescription);
			}
			smr.getEnums().put("com.gridnine.elsa.common.core.test.model.domain.TestEnum", enumDescription);
			dmr.getEnumsIds().add("com.gridnine.elsa.common.core.test.model.domain.TestEnum");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument");
			entityDescription.getAttributes().put("abstract", "true");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl");
			entityDescription.getAttributes().put("extends", "com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument");
			{
				var propertyDescription = new PropertyDescription("value");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("value", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.common.core.test.model.domain.TestDomainNestedDocumentImpl");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestGroup");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("items");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.core.test.model.domain.TestItem");
				entityDescription.getProperties().put("items", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestGroup", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.common.core.test.model.domain.TestGroup");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestItem");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestItem", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.common.core.test.model.domain.TestItem");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestDomainAsset");
			entityDescription.getAttributes().put("cache-resolve", "true");
			entityDescription.getAttributes().put("caption-expression", "stringProperty");
			entityDescription.getDisplayNames().put(new Locale("ru"), "Test Domain Assets");
			{
				var propertyDescription = new PropertyDescription("stringProperty");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("cache-find", "true");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "String property");
				entityDescription.getProperties().put("stringProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("dateTimeProperty");
				propertyDescription.setTagName("local-date-time-property");
				entityDescription.getProperties().put("dateTimeProperty", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestDomainAsset", entityDescription);
			dmr.getAssetsIds().add("com.gridnine.elsa.common.core.test.model.domain.TestDomainAsset");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
			entityDescription.getAttributes().put("cache-resolve", "true");
			entityDescription.getAttributes().put("caption-expression", "stringProperty");
			{
				var propertyDescription = new PropertyDescription("stringProperty");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("stringProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("getAllProperty");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("getAllProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("stringCollection");
				propertyDescription.setTagName("string-list");
				entityDescription.getProperties().put("stringCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityProperty");
				propertyDescription.setTagName("entity-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument");
				entityDescription.getProperties().put("entityProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumProperty");
				propertyDescription.setTagName("enum-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestEnum");
				entityDescription.getProperties().put("enumProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityReferenceProperty");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
				entityDescription.getProperties().put("entityReferenceProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityCollection");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.core.test.model.domain.BaseTestDomainNestedDocument");
				entityDescription.getProperties().put("entityCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("groups");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.core.test.model.domain.TestGroup");
				entityDescription.getProperties().put("groups", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumCollection");
				propertyDescription.setTagName("enum-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.core.test.model.domain.TestEnum");
				entityDescription.getProperties().put("enumCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityRefCollection");
				propertyDescription.setTagName("entity-reference-list");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
				entityDescription.getProperties().put("entityRefCollection", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument", entityDescription);
			dmr.getDocumentsIds().add("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocumentProjection");
			entityDescription.getAttributes().put("document", "com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
			entityDescription.getDisplayNames().put(new Locale("ru"), "Test documents");
			{
				var propertyDescription = new PropertyDescription("stringProperty");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("cache-find", "true");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "String  property");
				entityDescription.getProperties().put("stringProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("getAllProperty");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("cache-get-all", "true");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				entityDescription.getProperties().put("getAllProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("stringCollection");
				propertyDescription.setTagName("string-list");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "String  collection");
				entityDescription.getProperties().put("stringCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumProperty");
				propertyDescription.setTagName("enum-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestEnum");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "Enum  property");
				entityDescription.getProperties().put("enumProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityReferenceProperty");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				entityDescription.getProperties().put("entityReferenceProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumCollection");
				propertyDescription.setTagName("enum-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.common.core.test.model.domain.TestEnum");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "Enum collection");
				entityDescription.getProperties().put("enumCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityRefCollection");
				propertyDescription.setTagName("entity-reference-list");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.common.core.test.model.domain.TestDomainDocument");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(new Locale("ru"), "Entity ref collection");
				entityDescription.getProperties().put("entityRefCollection", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocumentProjection", entityDescription);
			dmr.getProjectionsIds().add("com.gridnine.elsa.common.core.test.model.domain.TestDomainDocumentProjection");
		}
	}
}