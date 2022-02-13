/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.ObjectReference;
import java.util.*;

public class TestDomainDocumentProjection extends BaseSearchableProjection<TestDomainDocument>{

	private String stringProperty;

	private TestEnum enumProperty;

	private ObjectReference<TestDomainDocument> entityReference;

	private final List<String> stringCollection = new ArrayList<>();

	private final List<TestEnum> enumCollection = new ArrayList<>();

	private final List<ObjectReference<TestDomainDocument>> entityRefCollection = new ArrayList<>();

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("enumProperty".equals(propertyName)){
			return enumProperty;
		}

		if("entityReference".equals(propertyName)){
			return entityReference;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		if("enumProperty".equals(propertyName)){
			this.enumProperty = (TestEnum) value;
			return;
		}

		if("entityReference".equals(propertyName)){
			//noinspection unchecked
			this.entityReference = (ObjectReference<TestDomainDocument>) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public Collection<?> getCollection(String collectionName){

		if("stringCollection".equals(collectionName)){
			return stringCollection;
		}

		if("enumCollection".equals(collectionName)){
			return enumCollection;
		}

		if("entityRefCollection".equals(collectionName)){
			return entityRefCollection;
		}

		return super.getCollection(collectionName);
	}
}