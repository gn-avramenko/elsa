/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common;

import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.custom.CustomTypesRegistry;
import java.util.ArrayList;

public class CoreCustomTypesConfigurator{

	public void configure(){
		var registry = Environment.getPublished(CustomTypesRegistry.class);
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
			tag.setTagName("enum-property");
			tag.setType("ENUM");
			tag.setObjectIdAttributeName("class-name");
			registry.getEntityTags().put("enum-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("long-property");
			tag.setType("LONG");
			registry.getEntityTags().put("long-property", tag);
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
				generic_1.setType("ENTITY");
				generic_1.setObjectIdAttributeName("class-name");
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
	}
}