/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.test.model.domain;

import com.gridnine.elsa.common.model.domain.BaseDocument;
import com.gridnine.elsa.common.model.domain.EntityReference;
import java.util.ArrayList;

public class TestDomainDocument extends BaseDocument{

	private String stringProperty;

	private String getAllProperty;

	private final ArrayList<String> stringCollection = new ArrayList<>();

	private BaseTestDomainNestedDocument entityProperty;

	private TestEnum enumProperty;

	private EntityReference<TestDomainDocument> entityReferenceProperty;

	private final ArrayList<BaseTestDomainNestedDocument> entityCollection = new ArrayList<>();

	private final ArrayList<TestGroup> groups = new ArrayList<>();

	private final ArrayList<TestEnum> enumCollection = new ArrayList<>();

	private final ArrayList<EntityReference<TestDomainDocument>> entityRefCollection = new ArrayList<>();

	public String getStringProperty(){
		return stringProperty;
	}

	public void setStringProperty(String value){
		this.stringProperty = value;
	}

	public String getGetAllProperty(){
		return getAllProperty;
	}

	public void setGetAllProperty(String value){
		this.getAllProperty = value;
	}

	public ArrayList<String> getStringCollection(){
		return stringCollection;
	}

	public BaseTestDomainNestedDocument getEntityProperty(){
		return entityProperty;
	}

	public void setEntityProperty(BaseTestDomainNestedDocument value){
		this.entityProperty = value;
	}

	public TestEnum getEnumProperty(){
		return enumProperty;
	}

	public void setEnumProperty(TestEnum value){
		this.enumProperty = value;
	}

	public EntityReference<TestDomainDocument> getEntityReferenceProperty(){
		return entityReferenceProperty;
	}

	public void setEntityReferenceProperty(EntityReference<TestDomainDocument> value){
		this.entityReferenceProperty = value;
	}

	public ArrayList<BaseTestDomainNestedDocument> getEntityCollection(){
		return entityCollection;
	}

	public ArrayList<TestGroup> getGroups(){
		return groups;
	}

	public ArrayList<TestEnum> getEnumCollection(){
		return enumCollection;
	}

	public ArrayList<EntityReference<TestDomainDocument>> getEntityRefCollection(){
		return entityRefCollection;
	}

	@Override
	public Object getValue(String propertyName){

		if("stringProperty".equals(propertyName)){
			return stringProperty;
		}

		if("getAllProperty".equals(propertyName)){
			return getAllProperty;
		}

		if("stringCollection".equals(propertyName)){
			return stringCollection;
		}

		if("entityProperty".equals(propertyName)){
			return entityProperty;
		}

		if("enumProperty".equals(propertyName)){
			return enumProperty;
		}

		if("entityReferenceProperty".equals(propertyName)){
			return entityReferenceProperty;
		}

		if("entityCollection".equals(propertyName)){
			return entityCollection;
		}

		if("groups".equals(propertyName)){
			return groups;
		}

		if("enumCollection".equals(propertyName)){
			return enumCollection;
		}

		if("entityRefCollection".equals(propertyName)){
			return entityRefCollection;
		}

		return super.getValue(propertyName);
	}

	@Override
	public void setValue(String propertyName, Object value){

		if("stringProperty".equals(propertyName)){
			this.stringProperty = (String) value;
			return;
		}

		if("getAllProperty".equals(propertyName)){
			this.getAllProperty = (String) value;
			return;
		}

		if("entityProperty".equals(propertyName)){
			this.entityProperty = (BaseTestDomainNestedDocument) value;
			return;
		}

		if("enumProperty".equals(propertyName)){
			this.enumProperty = (TestEnum) value;
			return;
		}

		if("entityReferenceProperty".equals(propertyName)){
			this.entityReferenceProperty = (EntityReference<TestDomainDocument>) value;
			return;
		}

		super.setValue(propertyName, value);
	}

	@Override
	public String toString(){
		return stringProperty;
	}
}