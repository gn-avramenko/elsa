/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test;

import com.gridnine.elsa.common.meta.common.EntityDescription;
import com.gridnine.elsa.common.meta.common.EnumDescription;
import com.gridnine.elsa.common.meta.common.EnumItemDescription;
import com.gridnine.elsa.common.meta.common.StandardCollectionDescription;
import com.gridnine.elsa.common.meta.common.StandardMapDescription;
import com.gridnine.elsa.common.meta.common.StandardPropertyDescription;
import com.gridnine.elsa.common.meta.common.StandardValueType;
import com.gridnine.elsa.common.meta.rest.RestDescription;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistry;
import com.gridnine.elsa.common.meta.rest.RestMetaRegistryConfigurator;
import org.springframework.stereotype.Component;

@Component
public class ElsaCommonCoreTestRestMetaRegistryConfigurator implements RestMetaRegistryConfigurator{

	@Override
	public void updateMetaRegistry(RestMetaRegistry registry){
		{
			var enumDescription = new EnumDescription("com.gridnine.elsa.common.core.test.model.rest.TestRestEnum");
			registry.getEnums().put(enumDescription.getId(), enumDescription);
			{
				var enumItemDescription = new EnumItemDescription("ELEMENT1");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
			{
				var enumItemDescription = new EnumItemDescription("ELEMENT2");
				enumDescription.getItems().put(enumItemDescription.getId(), enumItemDescription);
			}
		}
		{
			var entityDescription = new EntityDescription("com.gridnine.elsa.common.core.test.model.rest.TestRestEntity");
			registry.getEntities().put(entityDescription.getId(), entityDescription);
			{
				var propertyDescription = new StandardPropertyDescription("stringField");
				propertyDescription.setType(StandardValueType.STRING);
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var propertyDescription = new StandardPropertyDescription("enumField");
				propertyDescription.setType(StandardValueType.ENUM);
				propertyDescription.setClassName("com.gridnine.elsa.common.core.test.model.rest.TestRestEnum");
				entityDescription.getProperties().put(propertyDescription.getId(), propertyDescription);
			}
			{
				var collectionDescription = new StandardCollectionDescription("stringCollection");
				collectionDescription.setElementType(StandardValueType.STRING);
				collectionDescription.setUnique(true);
				entityDescription.getCollections().put(collectionDescription.getId(), collectionDescription);
			}
			{
				var mapDescription = new StandardMapDescription("stringMap");
				mapDescription.setKeyType(StandardValueType.STRING);
				mapDescription.setValueType(StandardValueType.STRING);
				entityDescription.getMaps().put(mapDescription.getId(), mapDescription);
			}
		}
		{
			var restDescription = new RestDescription("standard-test-rest");
			registry.getRests().put(restDescription.getId(), restDescription);
		}
	}
}