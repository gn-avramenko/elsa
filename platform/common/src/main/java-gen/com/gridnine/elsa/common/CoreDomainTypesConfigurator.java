/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common;

import com.gridnine.elsa.meta.common.AttributeDescription;
import com.gridnine.elsa.meta.common.AttributeType;
import com.gridnine.elsa.meta.common.DatabaseTagDescription;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.domain.DomainTypesRegistry;
import java.util.ArrayList;

public class CoreDomainTypesConfigurator{

	public static final String TAG_STRING_PROPERTY = "string-property";

	public static final String TAG_LOCAL_DATE_TIME_PROPERTY = "local-date-time-property";

	public static final String TAG_LOCAL_DATE_PROPERTY = "local-date-property";

	public static final String TAG_BOOLEAN_PROPERTY = "boolean-property";

	public static final String TAG_BIG_DECIMAL_PROPERTY = "big-decimal-property";

	public static final String TAG_BYTE_ARRAY_PROPERTY = "byte-array-property";

	public static final String TAG_LONG_PROPERTY = "long-property";

	public static final String TAG_ENTITY_REFERENCE_PROPERTY = "entity-reference-property";

	public static final String TAG_ENUM_PROPERTY = "enum-property";

	public static final String TAG_ENUM_LIST = "enum-list";

	public static final String TAG_STRING_LIST = "string-list";

	public static final String TAG_ENTITY_REFERENCE_LIST = "entity-reference-list";

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
			registry.getEntityTags().put("string-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-time-property");
			tag.setType("LOCAL-DATE-TIME");
			registry.getEntityTags().put("local-date-time-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-property");
			tag.setType("LOCAL-DATE");
			registry.getEntityTags().put("local-date-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("boolean-property");
			tag.setType("BOOLEAN");
			registry.getEntityTags().put("boolean-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("long-property");
			tag.setType("LONG");
			registry.getEntityTags().put("long-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("big-decimal-property");
			tag.setType("BIG-DECIMAL");
			registry.getEntityTags().put("big-decimal-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("byte-array-property");
			tag.setType("BYTE-ARRAY");
			registry.getEntityTags().put("byte-array-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("int-property");
			tag.setType("INT");
			registry.getEntityTags().put("int-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-reference-property");
			tag.setType("ENTITY-REFERENCE");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("class-name");
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-reference-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-property");
			tag.setType("ENTITY");
			tag.setObjectIdAttributeName("class-name");
			registry.getEntityTags().put("entity-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("enum-property");
			tag.setType("ENUM");
			tag.setObjectIdAttributeName("class-name");
			registry.getEntityTags().put("enum-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-list");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-reference-list");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("ENTITY-REFERENCE");
				var generics_2 = new ArrayList<GenericDescription>();
				{
					var generic_3 = new GenericDescription();
					generic_3.setId("class-name");
					generic_3.setType("ENTITY");
					generic_3.setObjectIdAttributeName("class-name");
					generics_2.add(generic_3);
				}
				generic_1.getNestedGenerics().addAll(generics_2);
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-reference-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("enum-list");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("ENUM");
				generic_1.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("enum-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("string-list");
			tag.setType("ARRAY-LIST");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("STRING");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("string-list", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-map");
			tag.setType("LINKED-HASH-MAP");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("key-class-name");
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("key-class-name");
				generics_0.add(generic_1);
			}
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("value-class-name");
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("value-class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getEntityTags().put("entity-map", tag);
		}
		{
			var tag = new DatabaseTagDescription();
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
			{
				var attr = new AttributeDescription();
				attr.setName("cache-get-all");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-get-all", attr);
			}
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasComparisonSupport(true);
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setHasStringOperationsSupport(true);
			tag.setSearchQueryArgumentType("STRING");
			registry.getDatabaseTags().put("string-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("local-date-time-property");
			tag.setType("LOCAL-DATE-TIME");
			tag.setType("LOCAL-DATE-TIME");
			tag.setHasComparisonSupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("LOCAL-DATE-TIME");
			registry.getDatabaseTags().put("local-date-time-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("local-date-property");
			tag.setType("LOCAL-DATE");
			tag.setType("LOCAL-DATE");
			tag.setHasComparisonSupport(true);
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("LOCAL-DATE-TIME");
			registry.getDatabaseTags().put("local-date-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("boolean-property");
			tag.setType("BOOLEAN");
			tag.setType("BOOLEAN");
			{
				var attr = new AttributeDescription();
				attr.setName("cache-get-all");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-get-all", attr);
			}
			{
				var attr = new AttributeDescription();
				attr.setName("cache-find");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-find", attr);
			}
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("BOOLEAN");
			registry.getDatabaseTags().put("boolean-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("big-decimal-property");
			tag.setType("BIG-DECIMAL");
			tag.setType("BIG-DECIMAL");
			tag.setHasComparisonSupport(true);
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("BIG-DECIMAL");
			registry.getDatabaseTags().put("big-decimal-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("byte-array-property");
			tag.setType("BYTE-ARRAY");
			tag.setType("BYTE-ARRAY");
			registry.getDatabaseTags().put("byte-array-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("long-property");
			tag.setType("LONG");
			tag.setType("LONG");
			tag.setHasComparisonSupport(true);
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("LONG");
			registry.getDatabaseTags().put("long-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("entity-reference-property");
			tag.setType("ENTITY-REFERENCE");
			tag.setType("ENTITY-REFERENCE");
			{
				var attr = new AttributeDescription();
				attr.setName("cache-get-all");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-get-all", attr);
			}
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("ENTITY-REFERENCE");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("class-name");
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("entity-reference-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("enum-property");
			tag.setType("ENUM");
			tag.setObjectIdAttributeName("class-name");
			tag.setType("ENUM");
			{
				var attr = new AttributeDescription();
				attr.setName("cache-find");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-find", attr);
			}
			{
				var attr = new AttributeDescription();
				attr.setName("cache-get-all");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("cache-get-all", attr);
			}
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasEqualitySupport(true);
			tag.setHasSortSupport(true);
			tag.setSearchQueryArgumentType("ENUM");
			registry.getDatabaseTags().put("enum-property", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("enum-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasCollectionSupport(true);
			tag.setSearchQueryArgumentType("ENUM");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("ENUM");
				generic_1.setObjectIdAttributeName("element-class-name");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("enum-list", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("string-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasCollectionSupport(true);
			tag.setSearchQueryArgumentType("STRING");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("STRING");
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("string-list", tag);
		}
		{
			var tag = new DatabaseTagDescription();
			tag.setTagName("entity-reference-list");
			tag.setType("ARRAY-LIST");
			tag.setType("ARRAY-LIST");
			{
				var attr = new AttributeDescription();
				attr.setName("use-in-text-search");
				attr.setType(AttributeType.BOOLEAN);
				attr.setDefaultValue("false");
				tag.getAttributes().put("use-in-text-search", attr);
			}
			tag.setHasCollectionSupport(true);
			tag.setSearchQueryArgumentType("ENTITY-REFERENCE");
			var generics_0 = new ArrayList<GenericDescription>();
			{
				var generic_1 = new GenericDescription();
				generic_1.setId("element-class-name");
				generic_1.setType("ENTITY-REFERENCE");
				var generics_2 = new ArrayList<GenericDescription>();
				{
					var generic_3 = new GenericDescription();
					generic_3.setId("class-name");
					generic_3.setType("ENTITY");
					generic_3.setObjectIdAttributeName("class-name");
					generics_2.add(generic_3);
				}
				generic_1.getNestedGenerics().addAll(generics_2);
				generics_0.add(generic_1);
			}
			tag.getGenerics().addAll(generics_0);
			registry.getDatabaseTags().put("entity-reference-list", tag);
		}
	}
}