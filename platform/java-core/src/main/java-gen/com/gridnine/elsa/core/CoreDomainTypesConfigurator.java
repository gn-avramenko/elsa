/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.core;

import com.gridnine.elsa.core.config.Environment;
import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.AttributeType;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import java.util.ArrayList;

public class CoreDomainTypesConfigurator{

	public void configure(){
		var registry = Environment.getPublished(DomainTypesRegistry.class);
		{
			var attr = new AttributeDescription();
			attr.setName("cache-resolve");
			attr.setType(AttributeType.BOOLEAN);
			attr.setDefaultValue("false");
			registry.getAssetAttributes().put("cache-resolve", attr);
		}
		{
			var attr = new AttributeDescription();
			attr.setName("cache-caption");
			attr.setType(AttributeType.BOOLEAN);
			attr.setDefaultValue("false");
			registry.getAssetAttributes().put("cache-caption", attr);
		}
		{
			var attr = new AttributeDescription();
			attr.setName("cache-resolve");
			attr.setType(AttributeType.BOOLEAN);
			attr.setDefaultValue("false");
			registry.getDocumentAttributes().put("cache-resolve", attr);
		}
		{
			var attr = new AttributeDescription();
			attr.setName("cache-caption");
			attr.setType(AttributeType.BOOLEAN);
			attr.setDefaultValue("false");
			registry.getDocumentAttributes().put("cache-caption", attr);
		}
		{
			var attr = new AttributeDescription();
			attr.setName("non-localizable");
			attr.setType(AttributeType.BOOLEAN);
			attr.setDefaultValue("false");
			registry.getEnumAttributes().put("non-localizable", attr);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("string-property");
			tag.setType("STRING");
			tag.setType("STRING");
			registry.getEntityTags().put("string-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-time-property");
			tag.setType("LOCAL-DATE-TIME");
			tag.setType("LOCAL-DATE-TIME");
			registry.getEntityTags().put("local-date-time-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-property");
			tag.setType("LOCAL-DATE");
			tag.setType("LOCAL-DATE");
			registry.getEntityTags().put("local-date-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("boolean-property");
			tag.setType("BOOLEAN");
			tag.setType("BOOLEAN");
			registry.getEntityTags().put("boolean-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("big-decimal-property");
			tag.setType("BIG-DECIMAL");
			tag.setType("BIG-DECIMAL");
			registry.getEntityTags().put("big-decimal-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("byte-array-property");
			tag.setType("BYTE-ARRAY");
			tag.setType("BYTE-ARRAY");
			registry.getEntityTags().put("byte-array-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-reference-property");
			tag.setType("ENTITY-REFERENCE");
			tag.setType("ENTITY-REFERENCE");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-reference-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-property");
			tag.setType("ENTITY");
			tag.setObjectIdAttributeName("class-name");
			tag.setType("ENTITY");
			registry.getEntityTags().put("entity-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("enum-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("ENUM");
				generic.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("enum-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("string-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("STRING");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("string-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-map");
			tag.setType("LINKED-HASH-MAP");
			tag.setType("LINKED-HASH-MAP");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("key-class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("key-class-name");
				generics_0.add(generic);
			}
			{
				var generic = new GenericDescription();
				generic.setId("value-class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("value-class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-map", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("string-property");
			tag.setType("STRING");
			tag.setType("STRING");
			{
				var attr = new AttributeDescription();
				attr.setName("cache-find");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-find", attr);
			}
			registry.getDatabaseTags().put("string-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-time-property");
			tag.setType("LOCAL-DATE-TIME");
			tag.setType("LOCAL-DATE-TIME");
			registry.getDatabaseTags().put("local-date-time-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-property");
			tag.setType("LOCAL-DATE");
			tag.setType("LOCAL-DATE");
			registry.getDatabaseTags().put("local-date-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("boolean-property");
			tag.setType("BOOLEAN");
			tag.setType("BOOLEAN");
			{
				var attr = new AttributeDescription();
				attr.setName("cache-find");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-find", attr);
			}
			registry.getDatabaseTags().put("boolean-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("big-decimal-property");
			tag.setType("BIG-DECIMAL");
			tag.setType("BIG-DECIMAL");
			registry.getDatabaseTags().put("big-decimal-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("byte-array-property");
			tag.setType("BYTE-ARRAY");
			tag.setType("BYTE-ARRAY");
			registry.getDatabaseTags().put("byte-array-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-reference-property");
			tag.setType("ENTITY-REFERENCE");
			tag.setType("ENTITY-REFERENCE");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("entity-reference-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("enum-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("ENTITY");
				generic.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("enum-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("string-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic = new GenericDescription();
				generic.setId("element-class-name");
				generic.setType("STRING");
				generics_0.add(generic);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("string-list", tag);
		}
	}
}