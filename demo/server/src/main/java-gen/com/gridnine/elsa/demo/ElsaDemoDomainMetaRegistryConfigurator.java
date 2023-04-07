/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.meta.common.EntityDescription;
import com.gridnine.elsa.meta.common.EnumDescription;
import com.gridnine.elsa.meta.common.EnumItemDescription;
import com.gridnine.elsa.meta.common.PropertyDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import java.util.Locale;

public class ElsaDemoDomainMetaRegistryConfigurator{

	public void configure(){
		var smr = Environment.getPublished(SerializableMetaRegistry.class);
		var dmr = Environment.getPublished(DomainMetaRegistry.class);
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.demo.model.domain.DemoEnum");
			{
				var enumItemDescription = new EnumItemDescription("ITEM1");
				enumItemDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Item 1");
				enumDescription.getItems().put("ITEM1", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ITEM2");
				enumItemDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Item 2");
				enumDescription.getItems().put("ITEM2", enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ITEM3");
				enumItemDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Item 3");
				enumDescription.getItems().put("ITEM3", enumItemDescription);
			}
			smr.getEnums().put("com.gridnine.elsa.demo.model.domain.DemoEnum", enumDescription);
			dmr.getEnumsIds().add("com.gridnine.elsa.demo.model.domain.DemoEnum");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument");
			entityDescription.getAttributes().put("abstract", "true");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoDomainNestedDocumentImpl");
			entityDescription.getAttributes().put("extends", "com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument");
			{
				var propertyDescription = new PropertyDescription("value");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("value", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoDomainNestedDocumentImpl", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.domain.DemoDomainNestedDocumentImpl");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoGroup");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("items");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.DemoItem");
				entityDescription.getProperties().put("items", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoGroup", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.domain.DemoGroup");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoItem");
			{
				var propertyDescription = new PropertyDescription("name");
				propertyDescription.setTagName("string-property");
				entityDescription.getProperties().put("name", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoItem", entityDescription);
			dmr.getEntitiesIds().add("com.gridnine.elsa.demo.model.domain.DemoItem");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoDomainAsset");
			entityDescription.getAttributes().put("cache-resolve", "true");
			entityDescription.getAttributes().put("caption-expression", "stringProperty");
			entityDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Test Domain Assets");
			{
				var propertyDescription = new PropertyDescription("stringProperty");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("cache-find", "true");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "String property");
				entityDescription.getProperties().put("stringProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("dateTimeProperty");
				propertyDescription.setTagName("local-date-time-property");
				entityDescription.getProperties().put("dateTimeProperty", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoDomainAsset", entityDescription);
			dmr.getAssetsIds().add("com.gridnine.elsa.demo.model.domain.DemoDomainAsset");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
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
				var propertyDescription = new PropertyDescription("dateTimeProperty");
				propertyDescription.setTagName("local-date-time-property");
				entityDescription.getProperties().put("dateTimeProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("intProperty");
				propertyDescription.setTagName("int-property");
				entityDescription.getProperties().put("intProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("longProperty");
				propertyDescription.setTagName("long-property");
				entityDescription.getProperties().put("longProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("bigDecimalProprerty");
				propertyDescription.setTagName("big-decimal-property");
				entityDescription.getProperties().put("bigDecimalProprerty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("booleanProperty");
				propertyDescription.setTagName("boolean-property");
				entityDescription.getProperties().put("booleanProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("dateProperty");
				propertyDescription.setTagName("local-date-property");
				entityDescription.getProperties().put("dateProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityProperty");
				propertyDescription.setTagName("entity-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument");
				entityDescription.getProperties().put("entityProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumProperty");
				propertyDescription.setTagName("enum-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoEnum");
				entityDescription.getProperties().put("enumProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityReferenceProperty");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				entityDescription.getProperties().put("entityReferenceProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityCollection");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.BaseDemoDomainNestedDocument");
				entityDescription.getProperties().put("entityCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("groups");
				propertyDescription.setTagName("entity-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.DemoGroup");
				entityDescription.getProperties().put("groups", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumCollection");
				propertyDescription.setTagName("enum-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.DemoEnum");
				entityDescription.getProperties().put("enumCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityRefCollection");
				propertyDescription.setTagName("entity-reference-list");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				entityDescription.getProperties().put("entityRefCollection", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoDomainDocument", entityDescription);
			dmr.getDocumentsIds().add("com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection");
			entityDescription.getAttributes().put("document", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
			entityDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Test documents");
			{
				var propertyDescription = new PropertyDescription("stringProperty");
				propertyDescription.setTagName("string-property");
				propertyDescription.getAttributes().put("cache-find", "true");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "String  property");
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
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "String  collection");
				entityDescription.getProperties().put("stringCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("dateTimeProperty");
				propertyDescription.setTagName("local-date-time-property");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Date time property");
				entityDescription.getProperties().put("dateTimeProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("longProperty");
				propertyDescription.setTagName("long-property");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Long property");
				entityDescription.getProperties().put("longProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("bigDecimalProprerty");
				propertyDescription.setTagName("big-decimal-property");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Big Decimal Property");
				entityDescription.getProperties().put("bigDecimalProprerty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("booleanProperty");
				propertyDescription.setTagName("boolean-property");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Boolean property");
				entityDescription.getProperties().put("booleanProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("dateProperty");
				propertyDescription.setTagName("local-date-property");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Date property");
				entityDescription.getProperties().put("dateProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumProperty");
				propertyDescription.setTagName("enum-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoEnum");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Enum  property");
				entityDescription.getProperties().put("enumProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityReferenceProperty");
				propertyDescription.setTagName("entity-reference-property");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				entityDescription.getProperties().put("entityReferenceProperty", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("enumCollection");
				propertyDescription.setTagName("enum-list");
				propertyDescription.getAttributes().put("element-class-name", "com.gridnine.elsa.demo.model.domain.DemoEnum");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru"), "Enum collection");
				entityDescription.getProperties().put("enumCollection", propertyDescription);
			}
			{
				var propertyDescription = new PropertyDescription("entityRefCollection");
				propertyDescription.setTagName("entity-reference-list");
				propertyDescription.getAttributes().put("class-name", "com.gridnine.elsa.demo.model.domain.DemoDomainDocument");
				propertyDescription.getAttributes().put("use-in-text-search", "true");
				entityDescription.getProperties().put("entityRefCollection", propertyDescription);
			}
			smr.getEntities().put("com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection", entityDescription);
			dmr.getProjectionsIds().add("com.gridnine.elsa.demo.model.domain.DemoDomainDocumentProjection");
		}
	}
}