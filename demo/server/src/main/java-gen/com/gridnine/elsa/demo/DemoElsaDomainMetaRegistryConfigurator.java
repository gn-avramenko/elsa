/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.demo;

import com.gridnine.elsa.common.core.utils.LocaleUtils;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyType;
import com.gridnine.elsa.common.meta.domain.DocumentDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistryConfigurator;
import com.gridnine.elsa.common.meta.domain.SearchableProjectionDescription;

public class DemoElsaDomainMetaRegistryConfigurator implements DomainMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(DomainMetaRegistry registry){
		{
			var documentDescription = new DocumentDescription("com.gridnine.elsa.demo.model.domain.DemoUserAccount");
			registry.getDocuments().put(documentDescription.getId(), documentDescription);
			documentDescription.setCacheResolve(true);
			{
				var propertyDescription = new StandardPropertyDescription("name");
				propertyDescription.setType(StandardValueType.STRING);
				documentDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("login");
				propertyDescription.setType(StandardValueType.STRING);
				documentDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
		{
			var projectionDescription = new SearchableProjectionDescription("com.gridnine.elsa.demo.model.domain.DemoUserAccountProjection");
			registry.getSearchableProjections().put(projectionDescription.getId(), projectionDescription);
			projectionDescription.setDocument("com.gridnine.elsa.demo.model.domain.DemoUserAccount");
			projectionDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "??????? ??????");
			{
				var propertyDescription = new DatabasePropertyDescription("name");
				propertyDescription.setType(DatabasePropertyType.STRING);
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "????????");
				projectionDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new DatabasePropertyDescription("login");
				propertyDescription.setType(DatabasePropertyType.STRING);
				propertyDescription.setCacheFind(true);
				propertyDescription.getDisplayNames().put(LocaleUtils.getLocale("ru",""), "?????");
				projectionDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
		}
	}
}