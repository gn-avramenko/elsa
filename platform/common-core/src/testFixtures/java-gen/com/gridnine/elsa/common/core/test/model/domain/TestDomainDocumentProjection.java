/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.core.test.model.domain;

import com.gridnine.elsa.common.core.model.domain.BaseSearchableProjection;
import com.gridnine.elsa.common.core.model.domain.EntityReference;
import java.util.*;

public class TestDomainDocumentProjection extends BaseSearchableProjection<TestDomainDocument>{

	private String stringProperty;

	private TestEnum enumProperty;

	private EntityReference<TestDomainDocument> entityReference;

	private final List<String> stringCollection = new ArrayList<>();

	private final List<TestEnum> enumCollection = new ArrayList<>();

	private final List<EntityReference<TestDomainDocument>> entityRefCollection = new ArrayList<>();

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public TestEnum getEnumProperty(){
		return enumProperty;
	}

	public void setEnumProperty(TestEnum value){
		this.enumProperty = value;
	}

	public EntityReference<TestDomainDocument> getEntityReference(){
		return entityReference;
	}

	public void setEntityReference(EntityReference<TestDomainDocument> value){
		this.entityReference = value;
	}

	public List<String> getStringCollection(){
		return stringCollection;
	}

	public List<TestEnum> getEnumCollection(){
		return enumCollection;
	}

	public List<EntityReference<TestDomainDocument>> getEntityRefCollection(){
		return entityRefCollection;
	}

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
			this.entityReference = (EntityReference<TestDomainDocument>) value;
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