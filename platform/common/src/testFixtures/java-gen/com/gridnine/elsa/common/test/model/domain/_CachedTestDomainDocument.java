/*****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.elsa.common.test.model.domain;

import com.gridnine.elsa.common.model.common.ReadOnlyArrayList;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.model.domain.CachedObject;
import com.gridnine.elsa.common.model.domain.EntityReference;
import com.gridnine.elsa.common.model.domain.VersionInfo;
import java.util.ArrayList;

public class _CachedTestDomainDocument extends TestDomainDocument implements CachedObject{

	private boolean allowChanges = false;

	private final ArrayList<String> stringCollection = new ReadOnlyArrayList<>();

	private final ArrayList<BaseTestDomainNestedDocument> entityCollection = new ReadOnlyArrayList<>();

	private final ArrayList<TestGroup> groups = new ReadOnlyArrayList<>();

	private final ArrayList<TestEnum> enumCollection = new ReadOnlyArrayList<>();

	private final ArrayList<EntityReference<TestDomainDocument>> entityRefCollection = new ReadOnlyArrayList<>();

	public void setAllowChanges(boolean allowChanges){
		this.allowChanges = allowChanges;
	}
	@Override
	public void setStringProperty(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setStringProperty(value);
	}
	@Override
	public void setGetAllProperty(String value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setGetAllProperty(value);
	}

	@Override
	public ArrayList<String> getStringCollection(){
		return this.stringCollection;
	}
	@Override
	public void setEntityProperty(BaseTestDomainNestedDocument value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityProperty(value);
	}
	@Override
	public void setEnumProperty(TestEnum value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEnumProperty(value);
	}
	@Override
	public void setEntityReferenceProperty(EntityReference<TestDomainDocument> value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setEntityReferenceProperty(value);
	}

	@Override
	public ArrayList<BaseTestDomainNestedDocument> getEntityCollection(){
		return this.entityCollection;
	}

	@Override
	public ArrayList<TestGroup> getGroups(){
		return this.groups;
	}

	@Override
	public ArrayList<TestEnum> getEnumCollection(){
		return this.enumCollection;
	}

	@Override
	public ArrayList<EntityReference<TestDomainDocument>> getEntityRefCollection(){
		return this.entityRefCollection;
	}
	@Override
	public void setVersionInfo(VersionInfo value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setVersionInfo(value);
	}
	@Override
	public void setId(long value){
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setId(value);
	}
	@Override
	public Object getValue(String propertyName){

		if("stringCollection".equals(propertyName)){
			return stringCollection;
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
		if(!allowChanges){
			throw Xeption.forDeveloper("changes are not allowed");
		}
		super.setValue(propertyName, value);
	}
}