/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.core;

import com.gridnine.elsa.core.config.Environment;
import com.gridnine.elsa.meta.common.GenericDescription;
import com.gridnine.elsa.meta.common.TagDescription;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import java.util.ArrayList;

public class CoreL10nTypesConfigurator{

	public void configure(){
		var registry = Environment.getPublished(L10nTypesRegistry.class);
		{
			var tag = new TagDescription();
			tag.setTagName("string-property");
			tag.setType("STRING");
			tag.setType("STRING");
			registry.getParameterTypeTags().put("string-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-time-property");
			tag.setType("LOCAL-DATE-TIME");
			tag.setType("LOCAL-DATE-TIME");
			registry.getParameterTypeTags().put("local-date-time-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("local-date-property");
			tag.setType("LOCAL-DATE");
			tag.setType("LOCAL-DATE");
			registry.getParameterTypeTags().put("local-date-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("boolean-property");
			tag.setType("BOOLEAN");
			tag.setType("BOOLEAN");
			registry.getParameterTypeTags().put("boolean-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("big-decimal-property");
			tag.setType("BIG-DECIMAL");
			tag.setType("BIG-DECIMAL");
			registry.getParameterTypeTags().put("big-decimal-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("byte-array-property");
			tag.setType("BYTE-ARRAY");
			tag.setType("BYTE-ARRAY");
			registry.getParameterTypeTags().put("byte-array-property", tag);
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
			registry.getParameterTypeTags().put("entity-reference-property", tag);
		}
		{
			var tag = new TagDescription();
			tag.setTagName("entity-property");
			tag.setType("ENTITY");
			tag.setObjectIdAttributeName("class-name");
			tag.setType("ENTITY");
			registry.getParameterTypeTags().put("entity-property", tag);
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
			registry.getParameterTypeTags().put("entity-list", tag);
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
			registry.getParameterTypeTags().put("enum-list", tag);
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
			registry.getParameterTypeTags().put("string-list", tag);
		}
	}
}